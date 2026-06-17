# CLAUDE.md — Mindeck

Инструкции для работы над проектом. Соблюдать обязательно.

---

## Архитектура и модули

**Проект:** Kotlin Multiplatform + Compose Multiplatform. Таргеты: Android (`androidApp`) и iOS (`iosApp`). Clean Architecture.

**Правило слоёв (инвариант):** `domain` — ядро, все зависимости направлены внутрь.
- `feature:* → domain`, `data → domain`; `domain` не зависит ни от чего.
- `feature` не знает про `data`. Граф собирается только в `app`.

**Ответственность модулей:**
- `domain` — модели, интерфейсы репозиториев, use cases (чистый Kotlin, только coroutines).
- `data` — Room (DAO, БД), реализации репозиториев, Koin-модули данных.
- `core:mvi` — базовые контракты MVI (`Store`, `BaseViewModel`).
- `core:ui` — дизайн-система (Compose): тема, кнопки, общие компоненты. Без `domain`.
- `feature:*` — экраны: ViewModel + State/Intent + Composable + свой Koin-модуль и `NavigationEvent`.
- `app` — связывает граф, навигация (Decompose), точка сборки DI.

**DI (Koin):** каждый модуль отдаёт свой Koin-модуль (`dataModule`, `commonDataModule`, `homeModule`, `cardModule`). Регистрируются в `initKoin` **и для Android (`KoinAndroid.kt`), и для iOS (`KoinIos.kt`)**. ⚠️ Новый feature-модуль дописывать в оба `initKoin`, иначе на старте белый экран.

**Навигация (Decompose):** `RootComponent` владеет `StackNavigation<Config>`; `Config` (sealed) — список экранов, `Child` (sealed) держит ViewModel. `createChild` достаёт VM через Koin `get<>()` и привязывает `lifecycle.doOnDestroy(vm::clear)`. Единый `Scaffold` в `Navigation.kt` владеет FAB и `SnackbarHost`. Feature отдаёт `NavigationEvent`, `app` маппит его в `Config`.

**MVI:** `Store<State, Intent, Effect>` (`state: StateFlow`, `effects: Flow`, `accept(intent)`); `BaseViewModel` c `viewModelScope`, `updateState { }`, `sendEffect()`, `clear()`. Экран — stateless: `Screen(state, onIntent, onNavigate, contentPadding)`. VM живёт в `Child`, состояние и колбэки прокидываются вниз (state hoisting).

---

## Правила написания кода

**Принципы:** Clean Architecture, SOLID, ООП. Экраны — по MVI (в проекте это фактический паттерн вместо «классического» MVVM).

**MVI-контракт экрана:**
- `State` — иммутабельный `data class`; `Intent` — `sealed interface`; навигация наружу — отдельный `NavigationEvent` (`sealed`).
- One-shot события (snackbar, результат операции) — через `Effect` (`sealed`, `sendEffect()`), **не через State**. В State допускается только флаг процесса (`isSubmitting`), не факт «успех/ошибка» (залипнет при рекомпозиции). VM без эффектов — `Effect = Nothing`.
- ViewModel наследует `BaseViewModel<State, Intent, Effect>`, меняет состояние только через `updateState { }`, не хранит Compose-типов.
- Composable-экран **stateless**: сигнатура `Screen(state, onIntent, onNavigate, contentPadding)`. Эффекты собираются через `ObserveEffects` (`core:ui`); маппинг эффекта в UI (текст/навигация) — в экране. Никакой бизнес-логики и обращений к репозиториям в Composable — только отрисовка и проброс intent'ов (state hoisting).

**Именование:**
- Composable — `PascalCase` (ktlint это разрешает через `.editorconfig`); экран — суффикс `Screen`, ViewModel — `ViewModel`, событие навигации — `NavigationEvent`.
- Идентификаторы и комментарии в коде — на английском.

**Ориентир на существующий код:**
- При написании нового кода ориентироваться на ранее написанный код пользователя — следовать его стилю, структуре и принятым в проекте подходам.
- Если встреченный код пользователя грязный, содержит ошибку или расходится с остальным его кодом — **сначала предложить поправить и его** (с обоснованием), и только после этого продолжать писать текущий код.

**Ресурсы (размеры, цвета, тексты):**
- Размеры и отступы брать из `MindeckTheme.dimensions.*` (`AppDimensions`), формы — из `MaterialTheme.shapes` / `MindeckTheme.shapes`. Голые `.dp` в UI не писать.
- Цвета — из `MaterialTheme.colorScheme.*` и `MindeckTheme.extraColors`; текстовые стили — из `MaterialTheme.typography.*`. Хардкод `Color(...)` запрещён.
- Тексты — только из ресурсов (`Res.string.*`).
- Если нужного цвета / размера / формы нет — **предложить добавить** его в токены (`AppColors` / `AppDimensions` / `AppShapes`) и дождаться согласия, потом продолжить.
- Если нужного текста нет — **добавить без спроса** в `strings.xml` (и `values-ru`), затем использовать.

**Форматирование:**
- Перед коммитом гонять `./gradlew spotlessApply` (Spotless + ktlint 1.5.0). Не спорить с форматтером — приводить код к его стилю.

**Compose:**
- UI-компоненты общего назначения держать в `core:ui`, не дублировать в feature.

**DI:** зависимости — только через конструктор (Koin), без сервис-локатора внутри классов (исключение — `RootComponent`, где VM достаётся через `get<>()`).

---

## Стандарты кода (внешние гайдлайны)

Это выжимка ключевого. **План Б:** если по тезисам непонятно, как именно писать конкретный код — открыть соответствующий гайд (ссылки в каждом блоке) и свериться с ним.

### Kotlin coding conventions

- `val` по умолчанию; `var` — только когда значение реально меняется. Возвращать/принимать иммутабельные типы (`List`, `Set`, `Map`), а не `ArrayList`/`HashSet`.
- Expression body там, где тело — одно выражение: `fun foo() = 1`. `Unit` в сигнатуре не писать.
- Property вместо функции, если расчёт дёшев, не бросает и стабилен при неизменном состоянии; иначе — функция.
- Backing property: `private val _items`, наружу `val items: List<…> get() = _items`.
- Именование: классы/объекты — `UpperCamelCase`; функции/свойства — `lowerCamelCase`; константы (`const`/deeply immutable) — `SCREAMING_SNAKE_CASE`; пакеты — lowercase без `_`. Аббревиатуры: 2 буквы — `IOStream`, 3+ — `XmlFormatter`.
- 4 пробела, без табов; `{` в конце строки. Trailing comma в многострочных списках параметров — да (чище диффы). Цепочки вызовов переносить с `.`/`?.` в начале строки.
- `if` для бинарного выбора, `when` для 3+ ветвей; обе формы — как выражения. Открытый диапазон `0..<n`, не `0..n-1`. Предпочитать `map`/`filter` циклам.
- Default-параметры вместо перегрузок-обёрток. String templates вместо конкатенации.
- Публичный API: явно указывать видимость и типы возврата/свойств, писать KDoc.
- Источник: <https://kotlinlang.org/docs/coding-conventions.html>

### Coroutines (structured concurrency)

- Никакого `GlobalScope`. Запуск только в скоупе с понятным жизненным циклом (`viewModelScope`, корутины Decompose-компонента); параллелизм внутри — через `coroutineScope { }`/`supervisorScope { }`.
- `suspend`-функции делать main-safe: переключение потока (`withContext(Dispatchers.IO/Default)`) — внутри функции, а не на стороне вызывающего.
- Dispatcher'ы инжектить (конструктор), не хардкодить — иначе не тестируется.
- `suspend`-функция должна завершать всю свою работу к моменту возврата (не оставлять «висящих» корутин). Отмену уважать: не глотать `CancellationException`.
- В `domain` — только `suspend`/`Flow` из coroutines, без платформенных API.
- Ориентир: <https://kotlinlang.org/docs/coding-conventions.html#coroutines>

### Compose API guidelines

- Composable, эмитящий UI, → `PascalCase`-существительное и возвращает `Unit` (не возвращает значения). Composable, возвращающий значение, — `lowerCamelCase`. `remember…`-фабрики — с префиксом `remember`.
- Параметр `modifier: Modifier = Modifier` — ровно один, именно с таким именем, **первый среди опциональных** (после обязательных). Применять к корневому элементу; дополнительные модификаторы конкатенировать только в конце.
- Порядок параметров: обязательные → опциональные (включая `modifier`) → trailing `content: @Composable () -> Unit` последним (для trailing-lambda синтаксиса). Slot API — через `content`-лямбды.
- State hoisting: предпочитать stateless-компоненты, принимающие состояние и `on…`-колбэки, внутреннему `remember`. Группы состояния выносить в hoisted state-холдер (интерфейс с суффиксом `State` + factory).
- Публичные типы-параметры аннотировать `@Stable`/`@Immutable` корректно (снять/поменять потом нельзя). `CompositionLocal` — `Local…` (префикс, не суффикс).
- Источники: <https://developer.android.com/develop/ui/compose/api-guidelines>, <https://github.com/androidx/androidx/blob/androidx-main/compose/docs/compose-api-guidelines.md>

### Compose performance

- Тяжёлые вычисления кэшировать через `remember` (с корректными ключами).
- В lazy-списках задавать стабильные `key` (и `contentType` для разнотипных списков).
- `derivedStateOf` для производного состояния, меняющегося чаще, чем результат (порог скролла и т.п.).
- **Откладывать чтение состояния** как можно ниже по фазам: для часто меняющихся значений (offset, alpha, scroll) — лямбда-модификаторы `Modifier.offset { }`, `graphicsLayer { }`, а не чтение в Composition.
- Не делать backwards write — не писать в state, который уже прочитан в этой композиции.
- Следить за skippability: нестабильные параметры ломают пропуск рекомпозиции (см. правила про `@Stable`/`@Immutable` выше).
- Производительность мерить **только в release + R8**, не в debug. Понимать фазы Composition → Layout → Draw.
- Источник: <https://developer.android.com/develop/ui/compose/performance>

### Android Kotlin style guide (поверх базовых конвенций)

- **Запрет wildcard-импортов** (`import …*`). Импорты сортировать.
- Лимит строки — 100 символов (исключения: URL/пакет/импорт).
- Не ловить общий `Exception`/`Throwable` без причины и не глотать исключения молча.
- Никакой венгерской нотации (`mName`, `s_name`) — кроме `_`-префикса у backing property.
- Однострочный `if/when` без скобок только если выражение целиком влезает в строку; многострочный — со скобками.
- Перенос: после операторов/infix; перед `.`/`?.`/`::`. KDoc обязателен для public/protected типов и членов.
- Источник: <https://developer.android.com/kotlin/style-guide>

---

## Команды и сборка

Таргеты: Android + iOS (`iosArm64`, `iosSimulatorArm64`). `appId` = `com.mindeck.app` (debug — суффикс `.debug`).

**Android:**
- Debug APK: `./gradlew :androidApp:assembleDebug`
- Установка на устройство/эмулятор: `./gradlew :androidApp:installDebug`

**iOS:**
- Модуль `app` собирается во framework для Xcode; открыть `iosApp/Mindeck.xcodeproj` и запускать на **симуляторе**.
- iOS гоняется **только на симуляторе** (среда: Sequoia + Xcode 26.3; macOS и iPhone не обновляются).

**Тесты:**
- Все таргеты сразу: `./gradlew allTests` (или `./gradlew test`).
- JVM: `./gradlew :domain:jvmTest`, iOS-симулятор: `./gradlew :domain:iosSimulatorArm64Test`.
- Модульно по аналогии: `:data:…`, и т.д.

**Форматирование:**
- Применить: `./gradlew spotlessApply`; проверить: `./gradlew spotlessCheck` (ktlint 1.5.0).

**Прочее:**
- `compileSdk`/`minSdk` заданы в `rootProject.extra` (корневой `build.gradle.kts`).

---

## Git и процесс

- Источник правил — **`CONTRIBUTING.md`** (GitHub Flow + Conventional Commits). Следовать ему.
- Ветки: `<type>/<issue-id>/<short-description>` (англ., lowercase, 2–4 слова через дефис). Issue создаётся **до** ветки.
- Коммиты: `<type>(<scope>): <description>` — lowercase, без точки в конце. `scope` — модуль/фича: `domain`, `data`, `card`, `home`, `core-ui`, `navigation`.
- В `develop` напрямую не коммитить — только ветка + PR. Если работа идёт на `develop`, сначала создать ветку.
- **Коммитить и пушить только по явной просьбе.** Сам по себе не коммитить.
- **Не добавлять `Co-Authored-By: Claude`** и любые подписи Claude в коммиты и тела PR.
- Перед коммитом прогонять `./gradlew spotlessApply`.

---

## Инструменты и поиск

**Поиск по коду:**
- Для поиска по коду **всегда использовать `ast-index`** (skill `ast-index:ast-index`), а не `Grep`/`Glob`.
- `grep`/поиск по тексту — только когда `ast-index` вернул пусто, либо нужен regex, строковый литерал в коде или текст в комментариях.
- Не дублировать результаты: если `ast-index` нашёл — это и есть полный ответ.

**Стиль общения:**
- Отвечать коротко, по сути, без воды.
- Если что-то сделано не так (грязный код, ошибка, расхождение со стилем) — сразу подмечать, обосновывать и предлагать изменение.
- Если всплывает что-то, что разработчику стоило бы знать, или что-то по мнению AI достаточно интересное/полезное — **предложить рассказать об этом подробнее** (не вываливать сразу, а спросить, нужно ли углубиться).
