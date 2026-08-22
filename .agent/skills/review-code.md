# Skill: Code Review

Code review in this repository: a PR, a diff, a single file, or a set of changes.

## Process

1. Determine the scope: which files/functions are affected (`git diff`, `git diff main...`).
2. Read the affected files in full — do not judge from the diff alone, out of context.
3. Check against the checklist below.
4. Format the result according to "Output format".

## Checklist

### Architecture (details in [architecture.md](architecture.md))
- [ ] The code lives in the correct feature package (`home_screen`, `settings`, ...), not dumped into a common package
- [ ] A screen does not pull dependencies past its ViewModel; navigation lambdas are passed down from above
- [ ] New screen is registered: an entry in `Screen` (sealed class) + `entry<...>` in `NfcHelperApp`

### Compose / UI
- [ ] Strings only via `stringResource(R.string...)`; hardcoded text in the UI is an error
  (historical example: `ReadTabContent.kt` with `"I am a read content"`)
- [ ] Composable functions are stateless: state comes from `viewModel.uiState.collectAsState()`,
  events go up as lambdas; no `remember { mutableStateOf }` for domain state
- [ ] The public screen component takes a `viewModel` + navigation lambdas;
  internal `Content`/`AppBar` are private and receive ready data
- [ ] `Modifier` is passed as a parameter with a default for reusable components
- [ ] Preview functions are not broken; `@OptIn(ExperimentalMaterial3Api::class)` is present where needed

### ViewModel / state
- [ ] State is a `StateFlow<XxxUiState>` with an explicit backing field
      (`val uiState: StateFlow<HomeUiState> field = MutableStateFlow(...)`),
      a single immutable `UiState` data class rather than a scatter of `MutableStateFlow`s
- [ ] Business logic has not leaked into composables

### General
- [ ] No commented-out dead code, `TODO`s without tasks, or debug logs
- [ ] Names follow Kotlin conventions; no `data class ... ()` with extra parentheses (`Screens.kt` style)
- [ ] Resources (strings, drawables) are named per the `<what>_<where>` template (`ic_settings_main_screen`,
      `title_settings_screen`)

## Output format

```markdown
## Review: <scope>

### Critical (blocks merge)
- `file:line` — problem → how to fix

### Worth improving
- `file:line` — remark → suggestion

### Good
- What was done well (1–3 points)
```

Do not inflate severity: style issues are not blockers; architecture violations and string
hardcodes are blockers.