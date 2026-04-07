# Interval Timer

Android-приложение для интервальных тренировок. Позволяет загружать программы тренировок по ID с сервера и выполнять их с таймером, визуальным прогрессом и звуковыми сигналами при переходе между интервалами.

## Стек технологий

Kotlin, Jetpack Compose, Material Design 3, Androidx Navigation 3, Koin, Retrofit + OkHttp, Kotlinx Serialization, SoundPool, Timber

## Архитектура

Clean Architecture с модульной структурой. Фичи используют паттерн MVVM + MVI (State / Action / Effect). Навигация — кастомный Navigator с back stack поверх androidx.navigation3. DI — Koin.

## Модули

### `app`

Точка входа приложения. Содержит `MainActivity` (single Activity на Compose), `IntervalTimerApplication` (инициализация Koin и Timber), навигацию (`NavigatorImpl` с mutable back stack) и тему Material 3 с поддержкой dynamic colors.

Зависит от всех остальных модулей.

### `core`

Общие компоненты, используемые фичами:

- **Navigator** — интерфейс навигации (`goTo`, `goBack`, `backStack`, `entryProvider`)
- **Routes** — `LoadingRoute` (стартовый экран) и `TrainingRoute` (экран тренировки, несёт `TrainingResponse`)
- **DemoColors** — палитра цветов приложения
- **IntExt** — `Int.toTimerFormat()` — форматирование секунд в `MM:SS` / `HH:MM:SS`

Зависит от `repository_api`.

### `repository_api`

Контракты и модели данных репозитория:

- **TrainingRepository** — интерфейс с методом `fetchTraining(id): FetchTrainingResult` (sealed: `Success` / `Fail`)
- **Модели данных** — `TrainingResponse` → `TrainingTimer` (id, title, totalTime, intervals) → `TrainingInterval` (title, time). Все `@Serializable`.

Не зависит от других модулей проекта.

### `repository_impl`

Реализация репозитория через Retrofit:

- **TrainingRepositoryImpl** — делегирует запрос в `TrainingApi`, оборачивает исключения в `Fail`
- **TrainingApi** — Retrofit-интерфейс, `GET /interval-timers/{id}` с токенами в заголовках
- **DI** — конфигурация OkHttpClient (30s таймауты), Retrofit (base URL, kotlinx.serialization converter), биндинг репозитория

Зависит от `repository_api`.

### `feature_loading_screen`

Экран загрузки — ввод ID тренировки и получение данных с сервера:

- **LoadingScreen** — UI на Compose: иконка, заголовок, поле ввода ID (по умолчанию "68"), кнопка загрузки, алерт ошибки
- **LoadingViewModel** — MVI: состояние загрузки, валидация ввода, вызов репозитория, навигация на `TrainingRoute` при успехе
- **LoadButton** — кнопка с тремя состояниями: Idle / Loading (progress indicator) / Error

Зависит от `core`, `repository_api`.

### `feature_training_screen`

Основной экран тренировки — таймер, прогресс, управление, звук:

- **TrainingScreen** — UI: top bar (название + статус + время), карточка тренировки, список интервалов с прогресс-барами, кнопки управления. Цветовая индикация состояний: Idle (серый), Running (зелёный), Paused (оранжевый), Completed (синий)
- **TrainingViewModel** — управление таймером (ticker каждые 1000мс), привязка к сервису для звука, обработка действий (Start/Pause/Resume/Reset/Back/New)
- **TrainingModel** — бизнес-логика: state machine тренировки (Idle → Running → Pause → Completed), отслеживание текущего интервала, учёт времени
- **TrainingMapper** — маппинг `TrainingModel` → UI state (статусы, оставшееся время, прогресс-бары, счётчик завершённых интервалов)
- **TrainingService** — foreground service для воспроизведения звуков (PlayStart / PlayInterval / PlayFinish)
- **TimerSoundPlayer** — SoundPool с тремя звуками (start, transition, finish), управление audio focus
- **UI-компоненты** — `TrainingCard`, `StatsView`, `IntervalsView` (auto-scroll к активному), `IntervalItemView` (номер, название, время, прогресс-бар), `ButtonsView` (контекстные кнопки по состоянию)

Зависит от `core`, `repository_api`.

## Граф зависимостей

```
app
├── core
│   └── repository_api
├── feature_loading_screen
│   ├── core
│   └── repository_api
├── feature_training_screen
│   ├── core
│   └── repository_api
└── repository_impl
    └── repository_api
```

## Поток данных

1. Пользователь вводит ID тренировки на `LoadingScreen`
2. `LoadingViewModel` вызывает `TrainingRepository.fetchTraining()`
3. Репозиторий загружает данные через Retrofit API
4. При успехе — навигация на `TrainingRoute` с `TrainingResponse`
5. `TrainingViewModel` создаёт `TrainingModel` и запускает таймер
6. Каждую секунду `TrainingMapper` обновляет UI state
7. При переходе между интервалами `TrainingService` воспроизводит звуковые сигналы
8. Пользователь управляет тренировкой через кнопки (Play / Pause / Resume / Reset)
