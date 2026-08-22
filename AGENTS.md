# AGENTS.md — NFC Helper

Rules for AI agents working with this repository.

## About the project

Android app "NFC Helper" (Kotlin + Jetpack Compose + Material 3).

- Module: `app` (the only one), package `com.maxim.nfchelper`
- Kotlin 2.x, plugin.serialization, compiler arg `-Xexplicit-backing-fields`
- Navigation: **Navigation 3** (`androidx.navigation3`) with `@Serializable sealed class Screen : NavKey`
- No DI framework; ViewModels are created via `viewModel()` inside `entry<...>`
- minSdk 29, targetSdk 37

## Commands

| Action | Command |
|---|---|
| Build | `./gradlew :app:assembleDebug` |
| Unit tests | `./gradlew :app:testDebugUnitTest` |

## Structure

```
com.maxim.nfchelper/
├── MainActivity.kt        # entry point: setContent { NFCHelperTheme { NfcHelperApp() } }
├── navigation/            # NfcHelperApp.kt (NavDisplay), Screens.kt (sealed class Screen)
├── home_screen/           # feature package: HomeScreen, HomeViewModel, ReadTabContent, WriteTabContent
├── settings/              # SettingsScreen, SettingsViewModel
└── ui/theme/              # Compose theme
```

Feature package pattern: `XxxScreen.kt` (public component + private Content/AppBar) +
`XxxViewModel.kt` (`StateFlow<XxxUiState>` with explicit backing field) + `XxxUiState`.

## Rules

1. User-facing communication language is Russian; code and comments are in English.
2. UI strings only via `strings.xml` (`stringResource(...)`), no hardcoding.
3. Screen state — ViewModel + `StateFlow<UiState>`; UI is stateless, events go up via lambdas.
4. Navigation — only via Navigation 3 (`backStack.add(Screen.X)`); a new screen = a new
   `Screen` entry + `entry<...>` in `NfcHelperApp`.
5. New feature = a separate package modeled after existing ones (`home_screen`, `settings`).
6. Before finishing a task, build the module and run unit tests (see commands above).

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

## Rules and skills

Style and process rules live in `.agent/rules/`, workflows — in `.agent/skills/`.

| File | When to use |
|---|---|
| [rules/code-style](.agent/rules/code-style.md) | Kotlin / Compose style, resources, ViewModel pattern |
| [rules/workflow](.agent/rules/workflow.md) | process: build/tests before finishing, git, change boundaries |
| [skills/build-and-verify](.agent/skills/build-and-verify.md) | build the project and verify changes |
| [skills/new-screen](.agent/skills/new-screen.md) | adding a new screen following the project template |
| [skills/review-code](.agent/skills/review-code.md) | code / PR / diff review |
| [skills/fix-bug](.agent/skills/fix-bug.md) | bug fixing |
| [skills/architecture](.agent/skills/architecture.md) | designing a new feature, architecture questions |
| [skills/testing-unit](.agent/skills/testing-unit.md) | writing and running unit tests |
