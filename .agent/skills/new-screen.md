# Skill: New Screen

Добавление нового экрана по шаблону проекта. Архитектурный контекст —
[architecture.md](architecture.md).

## Чеклист

### 1. Пакет фичи

Создай пакет `com.maxim.nfchelper.<feature>_screen/` (или `<feature>/`, если это не экран
с табами, а отдельная фича) по образцу `home_screen/` / `settings/`.

### 2. Три файла фичи

**`XxxScreen.kt`:**

```kotlin
@Composable
fun XxxScreen(
    viewModel: XxxViewModel,
    // навигационные лямбды по необходимости
) {
    val uiState = viewModel.uiState.collectAsState()
    XxxScreenContent(uiState = uiState.value /*, ... */)
}

@Composable
private fun XxxScreenContent(uiState: XxxUiState /*, ... */) {
    Scaffold(topBar = { XxxScreenAppBar(/* ... */) }) { innerPadding ->
        // контент
    }
}
```

- AppBar по образцу `MainScreenAppBar` / `SettingsScreenAppBar`
  (`TopAppBarDefaults.topAppBarColors(primaryContainer / primary)`).
- Для M3 API не забудь `@OptIn(ExperimentalMaterial3Api::class)`.

**`XxxViewModel.kt`:**

```kotlin
class XxxViewModel : ViewModel() {
    val uiState: StateFlow<XxxUiState>
        field = MutableStateFlow(XxxUiState())
}
```

**`XxxUiState`** (в файле ViewModel): immutable data class с дефолтами.

### 3. Ресурсы

- Заголовок и все строки → `strings.xml`, префикс `title_<screen>_screen` для тайтла.
- Иконки → `res/drawable/ic_*.xml`.
- Никакого хардкода текста в composables.

### 4. Навигация

1. В `navigation/Screens.kt` добавь:

   ```kotlin
   @Serializable
   data object Xxx : Screen()
   ```

2. В `NfcHelperApp.kt` зарегистрируй entry:

   ```kotlin
   entry<Xxx> {
       val viewModel: XxxViewModel = viewModel()
       XxxScreen(
           viewModel = viewModel,
           navigateBack = { backStack.removeLastOrNull() },
       )
   }
   ```

3. Переход из родительского экрана: пробрось лямбду `{ backStack.add(Screen.Xxx) }`
   сверху вниз (родительский Screen → Content → кнопка).

### 5. Проверка

Прогони [build-and-verify.md](build-and-verify.md). Если у экрана есть чистая логика в
ViewModel — добавь тесты по [testing-unit.md](testing-unit.md).

## Антипаттерны (не делать)

- Создавать экран без ViewModel «потому что пока пусто» — шаблон одинаковый для всех.
- Пробрасывать `NavBackStack`/навигацию вглубь фичи — только лямбды.
- Регистрировать route строкой — маршруты только через sealed class `Screen`.
