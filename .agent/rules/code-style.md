# Rules: Code Style (Kotlin / Compose)

Правила стиля кода для NFC Helper. Применяются к любым изменениям кода.

## Kotlin

- Официальные [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html);
  отступ — 4 пробела.
- Имена: классы `PascalCase`, функции/переменные `camelCase`, константы `SCREAMING_SNAKE_CASE`.
- Явные типы у public-функций; `val` вместо `var` везде, где возможно.
- Не оставлять закомментированный код и `TODO` без задачи.

## Compose

- Composable-функции, описывающие UI-сущности — с заглавной буквы
  (`HomeScreen`, `MainScreenAppBar`); служебные — по общим правилам Kotlin.
- Экран = public-компонент + private внутренности:

  ```kotlin
  @Composable
  fun HomeScreen(viewModel: HomeViewModel, navigateSettings: () -> Unit) { ... }

  @Composable
  private fun HomeScreenContent(uiState: HomeUiState, ...) { ... }
  ```

- Public-экран принимает `viewModel` и навигационные лямбды; данные наружу из ViewModel —
  только через `viewModel.uiState.collectAsState()`.
- Переиспользуемые компоненты принимают `modifier: Modifier = Modifier` первым
  (или последним) параметром.
- `@Preview`-функции — для переиспользуемых компонентов; `@OptIn(ExperimentalMaterial3Api::class)`
  ставится там, где использует M3-experimental API (`TopAppBar`, `PrimaryTabRow`).
- Состояние UI-виджетов (pager, tabs) — через `rememberXxxState()`; доменное состояние в
  Composable хранить запрещено (см. [architecture.md](../skills/architecture.md)).

## Ресурсы

- Строки — только `res/values/strings.xml` + `stringResource(R.string...)`.
  Хардкод текста в UI — блокирующая ошибка ревью.
- Имена ресурсов: `<тип>_<что>_<где>`:
  - строки: `title_main_screen`, `read_home_screen_pager_title`;
  - drawable: `ic_settings_main_screen`, `ic_back_top_app_bar`.

## Форматирование файлов

- Один top-level declaration на файл там, где это естественно
  (`HomeScreen.kt`, `HomeViewModel.kt`); маленькие private-типы экрана
  (как `TabItem` в `HomeScreen.kt`) могут жить в том же файле.
- Imports без wildcard (`import androidx.compose.material3.Text`), неиспользуемые удалять.

## ViewModel / состояние

- Паттерн проекта — explicit backing field (в проекте включён `-Xexplicit-backing-fields`):

  ```kotlin
  class HomeViewModel : ViewModel() {
      val uiState: StateFlow<HomeUiState>
          field = MutableStateFlow(HomeUiState())
  }
  ```

- UiState — один immutable data class на экран; не плодить много отдельных `MutableStateFlow`
  на одно состояние.
