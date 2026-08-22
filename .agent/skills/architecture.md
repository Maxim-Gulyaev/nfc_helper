# Skill: Architecture

Architectural context of NFC Helper and rules for designing new features.

## Locked-in decisions (do not change without agreement)

| Decision | Choice | Why |
|---|---|---|
| Architecture pattern | MVVM: `XxxViewModel` + `StateFlow<XxxUiState>` + stateless Compose | already adopted in `home_screen`, `settings` |
| DI | No framework; ViewModels via `viewModel()` inside `entry<...>` | the project is small, manual creation is enough |
| Navigation | Navigation 3 (`NavDisplay` + `rememberNavBackStack`), routes are `@Serializable sealed class Screen : NavKey` | type-safe keys instead of string routes |
| Serialization | kotlinx.serialization (for NavKey) | a Navigation 3 requirement |
| UI toolkit | Jetpack Compose + Material 3 | the project standard |
| Language level | `-Xexplicit-backing-fields` enabled — ViewModels use `field = MutableStateFlow(...)` | codebase convention |

## Layers

```
UI (composable, stateless)
   │  events up: lambdas
   ▼
ViewModel (StateFlow<UiState>, business logic)
   ▼
(future: data/repository layer — not present yet)
```

## How to add a new feature (template)

1. Package `com.maxim.nfchelper.<feature>_screen/` or `<feature>/`, by analogy with existing ones.
2. In the package:
   - `XxxScreen.kt` — a public component `(viewModel, navigation lambdas)` +
     private `XxxScreenContent` / `XxxScreenAppBar`;
   - `XxxViewModel.kt` — `class XxxViewModel : ViewModel()` with
     `val uiState: StateFlow<XxxUiState> field = MutableStateFlow(XxxUiState())`;
   - `data class XxxUiState(...)`.
3. Strings → `app/src/main/res/values/strings.xml` with a per-screen prefix
   (`title_<screen>_screen`, ...). Icons → `res/drawable/ic_*.xml`.
4. Route: add `@Serializable data object Xxx : Screen()` to `Screens.kt`,
   register `entry<Xxx> { ... }` in `NfcHelperApp.kt`.
5. Navigation from the parent screen: the lambda `{ backStack.add(Screen.Xxx) }`.
6. Build (`./gradlew :app:assembleDebug`) and run the tests.

## What counts as an architecture violation

- Domain state in a composable (`remember { mutableStateOf }` for business data).
- Hardcoded strings/text sizes in the UI.
- A screen knows about another screen directly (instead of a navigation lambda from above).
- NFC/IO logic inside composables or outside a ViewModel.
- A new navigation mechanism/new DI replacing the locked-in one.

## Open questions (ask the user when they arise)

- When real NFC work appears — will a separate data layer and repository be needed.
- Multi-modularity: the project is single-module for now, do not initiate a split without a request.