# Skill: Build & Verify

Сборка проекта и проверка изменений. Выполняется перед завершением любой задачи
и после любых правок кода.

## Шаг 1: Сборка

```bash
./gradlew :app:assembleDebug
```

- Успех → переходи к шагу 2.
- Ошибка компиляции → читай первый error (последующие часто — следствие), исправляй,
  повторяй. Типовые причины в этом проекте:
  - забыт `@OptIn(ExperimentalMaterial3Api::class)` для M3 API;
  - несуществующий `R.string...` / `R.drawable...` (опечатка или строка не добавлена
    в `strings.xml`);
  - explicit backing field: у `val uiState: StateFlow<...>` должно быть `field = ...`.

## Шаг 2: Unit-тесты

```bash
./gradlew :app:testDebugUnitTest
```

- Зелёные → готово.
- Падение → открой отчёт `app/build/reports/tests/testDebugUnitTest/index.html`,
  разбери упавший assert; если падение вызвано твоим изменением — фикс, если тест
  устарел относительно новой логики — согласуй изменение теста с пользователем.

## Шаг 3: Быстрая статическая самопроверка

Пройдись по diff'у с чеклистом из [review-code.md](review-code.md) (кратко):

- [ ] нет хардкода строк в UI;
- [ ] состояние через ViewModel/StateFlow, Composable stateless;
- [ ] навигация только через Navigation 3;
- [ ] неиспользуемые импорты удалены.

## Полезные варианты запуска

```bash
./gradlew :app:assembleDebug --console=plain -q      # тихий вывод
./gradlew :app:testDebugUnitTest --tests "com.maxim.nfchelper.home_screen.HomeViewModelTest"
./gradlew :app:clean :app:assembleDebug              # если подозреваешь stale-артефакты
```

Критерий завершения: обе команды прошли без ошибок, замечаний чеклиста нет.
