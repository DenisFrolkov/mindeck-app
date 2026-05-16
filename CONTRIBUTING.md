# Contributing Guide — Mindeck

Стратегия: **GitHub Flow** + **Conventional Commits**

---

## Ветки

### Шаблон

```
<type>/<issue-id>/<short-description>
```

- `type` — тип изменения (см. таблицу ниже)
- `issue-id` — номер GitHub Issue, **обязателен** (создать issue перед веткой)
- `short-description` — 2-4 слова через дефис, английский, lowercase

### Типы

| Тип | Когда использовать |
|---|---|
| `feat` | Новая функциональность |
| `fix` | Исправление бага |
| `refactor` | Рефакторинг без изменения поведения |
| `chore` | Зависимости, конфиги, сборка |
| `docs` | Документация |
| `test` | Тесты |
| `ci` | CI/CD пайплайн |
| `perf` | Оптимизация производительности |
| `style` | Форматирование, lint (без логики) |

### Примеры

```
feat/12/card-study-screen
fix/34/session-queue-crash
refactor/27/extract-viewmodel-types
chore/update-agp-version
docs/contributing-guide
ci/add-lint-workflow
```

---

## Коммиты

Формат по [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/):

```
<type>(<scope>): <description>
```

- `scope` — модуль или фича: `domain`, `data`, `presentation`, `card`, `deck`, `navigation`
- `description` — что сделано, lowercase, без точки в конце

### Примеры

```
feat(presentation): add card study screen
fix(data): protect session queue with mutex
refactor(presentation): move interval formatting to ui layer
chore(deps): update kotlin to 2.1.0
test(domain): add use case unit tests
ci: add github actions lint workflow
```

### Breaking change

```
feat(domain)!: replace CardRepository with split query/command interfaces

BREAKING CHANGE: CardRepository removed, use CardQueryRepository and CardCommandRepository
```

---

## Pull Requests

### Заголовок PR = первый коммит или суть ветки

```
<type>(<scope>): <description>
```

Примеры:
```
feat(presentation): add deck selection to creation card screen
fix(data): fix room migration from version 3 to 4
refactor(domain): extract use case types into subpackages
```

### Описание PR (шаблон)

```markdown
## Что сделано
- ...

## Почему
- ...

## Как проверить
- ...

closes #<issue-id>
```

> `closes #42` — GitHub автоматически закроет Issue при merge в `develop`.

---

## Правила GitHub Flow

1. `develop` — основная ветка, всегда рабочая
2. Любое изменение — через ветку + PR, напрямую не коммитить
3. Ветка живёт максимум 1-3 дня — дольше = риск конфликтов
4. PR требует ревью перед merge (даже соло — self-review)
5. После merge ветка удаляется
6. CI должен быть зелёным перед merge

---

## Связь с GitHub Issues

**Воркфлоу:**
1. Создать Issue на GitHub → получить номер (`#42`)
2. Создать ветку с этим номером
3. В описании PR написать `closes #42`
4. После merge в `develop` — Issue закроется автоматически

```
feat/42/dark-theme-support
      ↑
   issue #42
```

Типы задач для Issues: `bug`, `enhancement`, `refactor`, `chore`, `docs` — используй GitHub Labels.
