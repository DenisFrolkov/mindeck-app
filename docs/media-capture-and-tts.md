# Media Capture & TTS — выбор библиотек

Решение по тому, чем закрывать работу с медиа в карточках: фото, аудио и
озвучку текста (TTS). Документ — точка опоры на будущее: что берём, почему,
и как это ложится на слои Clean Architecture.

Статус: **принято** · 2026-06-20

---

## Область

Карточке нужно уметь принимать медиа из разных источников и озвучивать текст:

**Изображение**
- снять фото камерой;
- взять из галереи;
- взять из файлов;
- скачать по ссылке.

**Аудио**
- записать с микрофона;
- взять из файлов;
- скачать по ссылке.

**TTS**
- произнести текст вживую (без сохранения в файл).

---

## Принятые решения

1. **TTS не сохраняем в файл — только live-проговаривание по тапу.**
   TTS детерминирован от текста: сохранённое аудио протухнет при правке текста,
   а live-озвучка всегда совпадает с тем, что на экране. Плюс уходит
   единственный кусок, который пришлось бы писать руками (`expect/actual` для
   `synthesizeToFile` / `AVSpeechSynthesizer.write`).

2. **Всё закрывается готовыми KMP-библиотеками — своего платформенного кода ноль.**

3. **Медиа в `domain` хранится как путь/URL, не как бинар.**

4. **Произносимый текст — поле-уровень:** `PronounceableText(text, locale)`.
   Локаль обязательна — TTS-движку нужен язык, чтобы выбрать голос (иначе
   французское слово прочитается английским голосом). Inline-разметку внутри
   строки (`[[tts:...]]`) не вводим — парсер мини-языка не нужен.

---

## Стек

| Библиотека | На что | Таргеты | Примечание |
|---|---|---|---|
| **FileKit** (v0.10) | камера (фото), галерея, файлы, сохранение байт | Android, iOS, macOS, JVM, Wasm/JS | костяк по картинкам; `PlatformFile` на kotlinx-io; камеру и save-to-gallery добавили в 0.10 |
| **Ktor client** | скачивание по ссылке (фото и аудио) | все | живёт в `data` |
| **Kodio** *или* **kmp-record** | запись с микрофона → файл | JVM/Android/iOS/macOS/JS/Wasm | Kodio мощнее (`recording.saveAs(Path)` + плеер + транскрипция), но **версия 0.1.2 — ранняя**; kmp-record проще и только запись |
| **TextToSpeechKt** (`tts-compose`) | live-проговаривание | Android, iOS, macOS, web | `rememberTextToSpeechOrNull()`, настройка pitch/rate/voice |

> Координаты и версии перед добавлением в `libs.versions.toml` перепроверить —
> на момент написания context7 был недоступен, данные собраны через web-поиск.

---

## Карта по источникам

| Что нужно | Чем | Где в слоях |
|---|---|---|
| Снять фото | FileKit (camera picker) | пикер за интерфейсом → `app`/платформа |
| Из галереи | FileKit (image picker) | то же |
| Из файлов (фото/аудио) | FileKit (file picker) | то же |
| По ссылке (фото/аудио) | Ktor (URL → байты → путь) | **`data`** (репозиторий) |
| Сохранить байты | FileKit `PlatformFile.write` / kotlinx-io | data/платформа, одна точка |
| Записать аудио | Kodio / kmp-record | рекордер за интерфейсом |
| Произнести текст (TTS) | TextToSpeechKt | **`core:ui`** как UI-возможность |

---

## Архитектура по слоям

- **`domain`** — модели. Медиа = путь/URL, не бинар. Произносимое поле:
  ```kotlin
  data class PronounceableText(
      val text: String,
      val locale: String, // BCP-47, напр. "fr-FR" — нужен TTS для выбора голоса
  )
  ```
- **`data`** — `FileDownloader` на Ktor: «URL → сохранённый путь». Диспетчер
  инжектится через конструктор, тестируемо.
- **`core:ui`** — `rememberAppTextToSpeech()` (обёртка над TextToSpeechKt) +
  компонент кнопки-динамика. Зависимости от `domain` нет — типы примитивны
  (строка + локаль).
- **`app` / платформа** — реализации `MediaPicker` / `AudioRecorder` за
  интерфейсами из `domain`/`core`; сборка графа через Koin (и Android, и iOS
  `initKoin`).
- **`feature:card`** — дёргает интерфейсы. Сейчас `pickPhotoSource` /
  `pickAudioSource` / `confirmLink` в `CreateCardViewModel` — заглушки, сюда
  это и встаёт.

### TTS как презентация

Озвучка — UI-эффект (как снэкбар), а не бизнес-логика. Триггерится тапом по
кнопке: `tts.speak(text, locale)`. VM можно не трогать — это чистая отрисовка.
Если захочется единообразия «всё через intent» — завести `Intent.Pronounce`
→ `Effect.Speak`, по аналогии со снэкбаром; но без VM проще.

Обёртка `rememberAppTextToSpeech()` обязана закрыть:
- асинхронную инициализацию движка (Android — init-callback) и `dispose` —
  `rememberTextToSpeechOrNull()` это уже делает (отдаёт `null`, пока не готов);
- отсутствие голоса для нужной локали → деградировать (спрятать/задизейблить
  кнопку), а не падать.

---

## Вне области

- **Сохранение TTS в файл** — отказались осознанно (см. решение №1).
- **Воспроизведение готового аудио** (плеер для записанных/скачанных файлов) —
  если понадобится: Kodio (уже умеет playback) либо chaintech
  `ComposeMultiplatformMediaPlayer`. Сейчас не в области.
- **Inline TTS-разметка** внутри текста — не вводим.

---

## Следующий шаг

Контракты под реализацию:
- `MediaPicker` (камера / галерея / файлы);
- `AudioRecorder` (запись);
- `FileDownloader` (ссылка → путь, `data`);
- `rememberAppTextToSpeech()` + модель `PronounceableText`.

---

## Источники

- FileKit — <https://github.com/vinceglb/FileKit> · docs <https://filekit.mintlify.app/>
- Альтернативы по картинкам: Peekaboo, ImagePickerKMP — <https://ismoy.github.io/ImagePickerKMP/>
- Kodio — <https://github.com/dosier/kodio>
- kmp-record — <https://github.com/theolm/kmp-record>
- kmp-audio-recorder-player — <https://klibs.io/project/hyochan/kmp-audio-recorder-player>
- TextToSpeechKt — <https://github.com/Marc-JB/TextToSpeechKt>
- CopiloTTS (native + ONNX) — <https://github.com/sigmadeltasoftware/CopiloTTS>
- Каталог KMP-библиотек — <https://klibs.io>
