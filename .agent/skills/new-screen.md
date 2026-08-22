# Skill: New Screen

Adding a new screen following the project template. Architectural context —
[architecture.md](architecture.md).

## Checklist

### 1. Feature package

Create the package `com.maxim.nfchelper.<feature>_screen/` (or `<feature>/` if it is not a
tabbed screen but a separate feature) modeled after `home_screen/` / `settings/`.

### 2. Three feature files

**`XxxScreen.kt`:**

```kotlin
@Composable
fun XxxScreen(
    viewModel: XxxViewModel,
    // navigation lambdas as needed
) {
    val uiState = viewModel.uiState.collectAsState()
    XxxScreenContent(uiState = uiState.value /*, ... */)
}

@Composable
private fun XxxScreenContent(uiState: XxxUiState /*, ... */) {
    Scaffold(topBar = { XxxScreenAppBar(/* ... */) }) { innerPadding ->
        // content
    }
}
```

- The AppBar follows the pattern of `MainScreenAppBar` / `SettingsScreenAppBar`
  (`TopAppBarDefaults.topAppBarColors(primaryContainer / primary)`).
- For M3 APIs, do not forget `@OptIn(ExperimentalMaterial3Api::class)`.

**`XxxViewModel.kt`:**

```kotlin
class XxxViewModel : ViewModel() {
    val uiState: StateFlow<XxxUiState>
        field = MutableStateFlow(XxxUiState())
}
```

**`XxxUiState`** (in the ViewModel file): immutable data class with defaults.

### 3. Resources

- The title and all strings → `strings.xml`, prefix `title_<screen>_screen` for the title.
- Icons → `res/drawable/ic_*.xml`.
- No hardcoded text in composables.

### 4. Navigation

1. In `navigation/Screens.kt` add:

   ```kotlin
   @Serializable
   data object Xxx : Screen()
   ```

2. Register the entry in `NfcHelperApp.kt`:

   ```kotlin
   entry<Xxx> {
       val viewModel: XxxViewModel = viewModel()
       XxxScreen(
           viewModel = viewModel,
           navigateBack = { backStack.removeLastOrNull() },
       )
   }
   ```

3. Navigation from the parent screen: pass down the lambda `{ backStack.add(Screen.Xxx) }`
   (parent Screen → Content → button).

### 5. Verification

Run [build-and-verify.md](build-and-verify.md). If the screen has pure logic in its
ViewModel — add tests following [testing-unit.md](testing-unit.md).

## Anti-patterns (do not)

- Creating a screen without a ViewModel "because it is empty for now" — the template is the same for all.
- Passing `NavBackStack`/navigation deep into a feature — only lambdas.
- Registering routes as strings — routes go only through the sealed class `Screen`.