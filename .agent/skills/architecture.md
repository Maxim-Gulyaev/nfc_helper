# Skill: Architecture

Архитектурный контекст NFC Helper и правила проектирования новых фич.

## Зафиксированные решения (не менять без согласования)

| Решение | Выбор | Почему |
|---|---|---|
| Архитектурный паттерн | MVVM: `XxxViewModel` + `StateFlow<XxxUiState>` + stateless Compose | уже принят в `home_screen`, `settings` |
| DI | Нет фреймворка; ViewModel через `viewModel()` внутри `entry<...>` | проект маленький, ручное создание достаточно |
| Навигация | Navigation 3 (`NavDisplay` + `rememberNavBackStack`), маршруты — `@Serializable sealed class Screen : NavKey` | типобезопасные ключи вместо строковых роутов |
| Сериализация | kotlinx.serialization (для NavKey) | требование Navigation 3 |
| UI-кит | Jetpack Compose + Material 3 | стандарт проекта |
| Языковой уровень | `-Xexplicit-backing-fields` включён — ViewModel'ы используют `field = MutableStateFlow(...)` | конвенция кодовой базы |

## Слои

```
UI (Composable, stateless)
   │  события вверх: лямбды
   ▼
ViewModel (StateFlow<UiState>, бизнес-логика)
   ▼
(будущее: data/repository слой — пока отсутствует)
```

## Как добавить новую фичу (шаблон)

1. Пакет `com.maxim.nfchelper.<feature>_screen/` или `<feature>/` по аналогии с существующими.
2. В пакете:
   - `XxxScreen.kt` — public-компонент `(viewModel, навигационные лямбды)` +
     private `XxxScreenContent` / `XxxScreenAppBar`;
   - `XxxViewModel.kt` — `class XxxViewModel : ViewModel()` с
     `val uiState: StateFlow<XxxUiState> field = MutableStateFlow(XxxUiState())`;
   - `data class XxxUiState(...)`.
3. Строки → `app/src/main/res/values/strings.xml` с префиксом по экрану
   (`title_<screen>_screen`, ...). Иконки → `res/drawable/ic_*.xml`.
4. Маршрут: добавить `@Serializable data object Xxx : Screen()` в `Screens.kt`,
   зарегистрировать `entry<Xxx> { ... }` в `NfcHelperApp.kt`.
5. Переход из родительского экрана: лямбда `{ backStack.add(Screen.Xxx) }`.
6. Собрать (`./gradlew :app:assembleDebug`) и прогнать тесты.

## Что считается нарушением архитектуры

- Доменное состояние в Composable (`remember { mutableStateOf }` для бизнес-данных).
- Хардкод строк/размеров текста в UI.
- Экран знает о другом экране напрямую (вместо лямбды навигации сверху).
- Логика NFC/IO внутри Composable или вне ViewModel.
- Новый механизм навигации/новый DI взамен зафиксированного.

## Открытые вопросы (спросить пользователя при появлении)

- Когда появится реальная работа с NFC — понадобится ли отдельный data-слой и репозиторий.
- Многомодульность: пока проект одномодульный, разбивку не инициировать без запроса.
