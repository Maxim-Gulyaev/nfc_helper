# AGENTS.md — NFC Helper

Правила для ИИ-агентов, работающих с этим репозиторием.

## О проекте

Android-приложение «NFC Helper» (Kotlin + Jetpack Compose + Material 3).

- Модуль: `app` (единственный), пакет `com.maxim.nfchelper`
- Kotlin 2.x, plugin.serialization, compiler arg `-Xexplicit-backing-fields`
- Navigation: **Navigation 3** (`androidx.navigation3`) с `@Serializable sealed class Screen : NavKey`
- DI-фреймворка нет; ViewModel создаются через `viewModel()` внутри `entry<...>`
- minSdk 29, targetSdk 37

## Команды

| Действие | Команда |
|---|---|
| Сборка | `./gradlew :app:assembleDebug` |
| Unit-тесты | `./gradlew :app:testDebugUnitTest` |

## Структура

```
com.maxim.nfchelper/
├── MainActivity.kt        # точка входа: setContent { NFCHelperTheme { NfcHelperApp() } }
├── navigation/            # NfcHelperApp.kt (NavDisplay), Screens.kt (sealed class Screen)
├── home_screen/           # фича-пакет: HomeScreen, HomeViewModel, ReadTabContent, WriteTabContent
├── settings/              # SettingsScreen, SettingsViewModel
└── ui/theme/              # тема Compose
```

Паттерн фича-пакета: `XxxScreen.kt` (public-компонент + private Content/AppBar) +
`XxxViewModel.kt` (`StateFlow<XxxUiState>` через explicit backing field) + `XxxUiState`.

## Правила

1. Язык общения с пользователем — русский; код и комментарии — на английском.
2. Строки в UI — только через `strings.xml` (`stringResource(...)`), без хардкода.
3. Состояние экрана — ViewModel + `StateFlow<UiState>`; UI — stateless, события вверх лямбдами.
4. Навигация — только через Navigation 3 (`backStack.add(Screen.X)`); новый экран = новая запись
   `Screen` + `entry<...>` в `NfcHelperApp`.
5. Новая фича = отдельный пакет по образцу существующих (`home_screen`, `settings`).
6. Перед завершением задачи собирать модуль и запускать unit-тесты (см. команды выше).

## Mandatory Android Post-Implementation Review

For every implementation task in an Android project, perform a mandatory self-review before declaring the task complete.

After implementing the requested changes:

1. Inspect the complete diff and verify that the implementation matches the task requirements and existing project conventions.
2. Review the changed code and related execution flow for:
   - incorrect logic, missed requirements, and regressions;
   - edge cases, nullability, error handling, and state consistency;
   - coroutine cancellation, threading, lifecycle, and resource leaks;
   - Jetpack Compose state handling and avoidable recompositions;
   - navigation, dependency injection, module boundaries, and Gradle configuration where relevant;
   - compatibility with all affected build variants and product flavors.
3. Run the most relevant available verification: targeted unit tests, compilation, lint, or an affected build variant. Prefer targeted checks first.
4. Fix every issue found during the review that is within the task scope.
5. Re-run the relevant review and verification after making fixes. Repeat until no actionable issues remain or further progress is genuinely blocked.

Self-review is an implementation step, not a report-only step: make the necessary corrections instead of merely listing them.

Do not introduce unrelated refactoring or revert pre-existing user changes.

Do not declare the task complete while relevant checks are failing. If a check cannot be run or an issue cannot be resolved, explicitly report the blocker, the unverified area, and the remaining risk.

## Правила и скиллы

Правила стиля и процесса лежат в `.agent/rules/`, сценарии работы — в `.agent/skills/`.

| Файл | Когда использовать |
|---|---|
| [rules/code-style](.agent/rules/code-style.md) | стиль Kotlin / Compose, ресурсы, ViewModel-паттерн |
| [rules/workflow](.agent/rules/workflow.md) | процесс: сборка/тесты перед завершением, git, границы изменений |
| [skills/build-and-verify](.agent/skills/build-and-verify.md) | собрать проект и проверить изменения |
| [skills/new-screen](.agent/skills/new-screen.md) | добавление нового экрана по шаблону проекта |
| [skills/review-code](.agent/skills/review-code.md) | ревью кода / PR / diff |
| [skills/fix-bug](.agent/skills/fix-bug.md) | исправление бага |
| [skills/architecture](.agent/skills/architecture.md) | проектирование новой фичи, вопросы архитектуры |
| [skills/testing-unit](.agent/skills/testing-unit.md) | написание и запуск unit-тестов |
