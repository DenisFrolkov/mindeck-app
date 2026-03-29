# Mindeck

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-26-green.svg)](https://developer.android.com/about/versions/oreo)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-purple.svg)](https://kotlinlang.org)

A clean, minimalist Android flashcard app built with Jetpack Compose and powered by the **SM-2 spaced repetition algorithm**. Cards surface at the right moment — often enough to remember, rarely enough to stay efficient.

Built as a portfolio project demonstrating Clean Architecture, CQRS, and modern Android development practices.

---

## Features

- Create and organize cards into decks
- Two card types: Simple and Complex
- Spaced repetition study sessions based on SM-2
- Four-button rating system: **Again / Hard / Good / Easy**
- Daily limit of 20 new cards
- Cards progress through states: `NEW → LEARNING → REVIEW` (with `LAPSE` on forgetting)

---

## How Spaced Repetition Works

Mindeck implements the [SM-2 algorithm](https://www.super-memory.com/english/ol/sm2.htm) (Piotr Wozniak, 1990) with Anki-style adjustments for Hard/Easy multipliers.

Each card tracks its ease factor, review interval, and learning step. After each study session the next review date is calculated automatically — cards due today are shown first, new cards are capped at 20/day.

Full algorithm details: [`docs/spaced-repetition.md`](docs/spaced-repetition.md)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.2.20 |
| UI | Jetpack Compose + Material3 |
| Navigation | Navigation3 1.0.0 (alpha) |
| DI | Hilt 2.57.2 |
| Database | Room 2.7.0 |
| Async | Coroutines 1.10.2 + StateFlow |
| Build | AGP 8.13.0, KSP 2.2.20 |
| Min SDK | 26 (Android 8.0) |

---

## Architecture

Clean Architecture with 4 Gradle modules:

```
app/                  ← Entry point (MainActivity, App)
├── presentation/     ← Compose UI, ViewModels, Navigation, States
├── data/             ← Room DB, DAOs, Mappers, Repository impls, Hilt modules
└── domain/           ← Models, Repository interfaces, Use Cases (CQRS)
```

Use cases follow CQRS — split into `command/` (writes) and `query/` (reads):
- Commands: `CreateCardUseCase`, `DeleteCardUseCase`, `RenameDeckUseCase`, …
- Queries: `GetAllCardsUseCase`, `GetCardsRepetitionUseCase`, …

Navigation uses a custom stack-based navigator with serializable routes:
`MainRoute → DecksRoute → DeckRoute → CardRoute`

---

## Getting Started

### Prerequisites

- Android Studio Meerkat or newer
- JDK 17
- Android SDK 36

### Build from source

```bash
git clone https://github.com/DenisFrolkov/mindeck-app.git
cd mindeck-app
./gradlew assembleDebug
```

APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

### Download APK

A signed release APK is available on the [Releases](https://github.com/DenisFrolkov/mindeck-app/releases) page.

---

## Author

**Denis Frolkov** — [GitHub](https://github.com/DenisFrolkov) · [LinkedIn](https://www.linkedin.com/in/denis-frolkov/) · [Telegram](https://t.me/o2232) · [Email](mailto:denisfrolkov3@gmail.com)

---

## License

[MIT](LICENSE)
