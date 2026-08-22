# Rules: Code Style (Kotlin / Compose)

Code style rules for NFC Helper. They apply to any code changes.

## Kotlin

- Official [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html);
  indentation is 4 spaces.
- Naming: classes `PascalCase`, functions/variables `camelCase`, constants `SCREAMING_SNAKE_CASE`.
- Explicit return types on public functions; `val` instead of `var` wherever possible.
- Do not leave commented-out code or `TODO`s without a task.

## Compose

- Composable functions describing UI entities are capitalized
  (`HomeScreen`, `MainScreenAppBar`); utility ones follow general Kotlin rules.
- A screen = public component + private internals:

  ```kotlin
  @Composable
  fun HomeScreen(viewModel: HomeViewModel, navigateSettings: () -> Unit) { ... }

  @Composable
  private fun HomeScreenContent(uiState: HomeUiState, ...) { ... }
  ```

- The public screen takes a `viewModel` and navigation lambdas; data flows out of the ViewModel
  only via `viewModel.uiState.collectAsState()`.
- Reusable components take `modifier: Modifier = Modifier` as the first
  (or last) parameter.
- `@Preview` functions — for reusable components; add `@OptIn(ExperimentalMaterial3Api::class)`
  where M3-experimental APIs are used (`TopAppBar`, `PrimaryTabRow`).
- State of UI widgets (pager, tabs) — via `rememberXxxState()`; storing domain state in a
  composable is forbidden (see [architecture.md](../skills/architecture.md)).

## Comments

- Do not pollute the code with optional comments: a comment is only justified when it explains
  a non-obvious **why** (a non-obvious decision, a limitation, a link to a task).
- Do not describe obvious actions in comments ("calling function X", "updating state") —
  that is readable from the code itself.
- Prefer a descriptive name over a comment (`ThemeMode.fromName` instead of `parseString`).

## Resources

- Strings only via `res/values/strings.xml` + `stringResource(R.string...)`.
  Hardcoded text in the UI is a blocking review error.
- Resource naming: `<type>_<what>_<where>`:
  - strings: `title_main_screen`, `read_home_screen_pager_title`;
  - drawable: `ic_settings_main_screen`, `ic_back_top_app_bar`.

## File formatting

- One top-level declaration per file where it feels natural
  (`HomeScreen.kt`, `HomeViewModel.kt`); small screen-private types
  (like `TabItem` in `HomeScreen.kt`) may live in the same file.
- Imports without wildcards (`import androidx.compose.material3.Text`); remove unused ones.

## ViewModel / state

- The project pattern is an explicit backing field (the project enables `-Xexplicit-backing-fields`):

  ```kotlin
  class HomeViewModel : ViewModel() {
      val uiState: StateFlow<HomeUiState>
          field = MutableStateFlow(HomeUiState())
  }
  ```

- UiState is one immutable data class per screen; do not spawn multiple separate `MutableStateFlow`s
  for one state.