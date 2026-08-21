# Skill: Code Review

Ревью кода в этом репозитории: PR, diff, отдельный файл или набор изменений.

## Порядок работы

1. Определи скоуп: какие файлы/функции затронуты (`git diff`, `git diff main...`).
2. Прочитай затронутые файлы целиком — не суди по diff в отрыве от контекста.
3. Проверь по чеклисту ниже.
4. Оформи результат в формате «Формат вывода».

## Чеклист

### Архитектура (подробности в [architecture.md](architecture.md))
- [ ] Код лежит в правильном фича-пакете (`home_screen`, `settings`, ...), а не свален в общий пакет
- [ ] Экран не тянет зависимости мимо ViewModel; навигационные лямбды пробрасываются сверху
- [ ] Новый экран зарегистрирован: запись в `Screen` (sealed class) + `entry<...>` в `NfcHelperApp`

### Compose / UI
- [ ] Строки только через `stringResource(R.string...)`; хардкод текста в UI — ошибка
  (исторический пример: `ReadTabContent.kt` с `"I am a read content"`)
- [ ] Composable-функции stateless: состояние из `viewModel.uiState.collectAsState()`,
  события — лямбды вверх; никаких `remember { mutableStateOf }` для доменного состояния
- [ ] Public-компонент экрана принимает `viewModel` + навигационные лямбды;
  внутренние `Content`/`AppBar` — private и получают готовые данные
- [ ] `Modifier` передаётся параметром с дефолтом у переиспользуемых компонентов
- [ ] Preview-функции не сломаны, `@OptIn(ExperimentalMaterial3Api::class)` стоит там, где нужно

### ViewModel / состояние
- [ ] Состояние — `StateFlow<XxxUiState>` с explicit backing field
      (`val uiState: StateFlow<HomeUiState> field = MutableStateFlow(...)`),
      единый immutable `UiState` data class, а не россыпь `MutableStateFlow`
- [ ] Бизнес-логика не протекла в Composable

### Общее
- [ ] Нет закомментированного мёртвого кода, `TODO` без задачи, отладочных логов
- [ ] Имена по Kotlin conventions; нет `data class ... ()` с лишними скобками (стиль `Screens.kt`)
- [ ] Ресурсы (строки, drawable) названы по шаблону `<что>_<где>` (`ic_settings_main_screen`,
      `title_settings_screen`)

## Формат вывода

```markdown
## Review: <scope>

### Критично (блокирует мерж)
- `файл:строка` — проблема → как исправить

### Стоит улучшить
- `файл:строка` — замечание → предложение

### Ок
- Что сделано хорошо (1–3 пункта)
```

Серьёзность завышать не нужно: стилистика — не блокер, нарушение архитектуры и строковые
хардкоды — блокер.
