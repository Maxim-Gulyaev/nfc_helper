# Skill: Unit Testing (только local unit tests)

Правила написания и запуска **unit**-тестов (`app/src/test/`). Instrumentation-тесты
(`app/src/androidTest/`) в скилл не входят.

## Запуск

```bash
./gradlew :app:testDebugUnitTest              # все unit-тесты
./gradlew :app:testDebugUnitTest --tests "com.maxim.nfchelper.home_screen.HomeViewModelTest"
```

Отчёт: `app/build/reports/tests/testDebugUnitTest/index.html`.

## Текущая инфраструктура

- Подключён **только JUnit4** (`testImplementation(libs.junit)`).
- НЕ подключены: `kotlinx-coroutines-test`, `turbine`, `mockk`/`mockito`, Robolectric.
- Существующие тесты: шаблонный `ExampleUnitTest.kt` (можно считать примером стиля JUnit4).

## Что тестируем

| Код | Тестируем? | Как |
|---|---|---|
| ViewModel / UiState-логика (чистые переходы состояния) | да | JUnit4, без Android-зависимостей |
| Чистые функции/утилиты (парсинг, форматирование, валидация) | да | обычный JUnit4 |
| Composable UI | нет (unit) | — вне скоупа; только сборка/ручная проверка |
| Код с `Context`, NFC-адаптером, Android API | нет напрямую | выносить логику в чистый Kotlin и тестировать её |

Если для теста нужны корутины/моки — **сначала согласовать с пользователем** добавление
зависимостей (`kotlinx-coroutines-test`, `turbine`, `mockk`) в `app/build.gradle.kts`.
До согласования писать тесты, которым хватает чистого JUnit4 (например, проверять
`MutableStateFlow.value` напрямую без `viewModelScope`).

## Расположение и имена

- Зеркалим пакет: класс `com.maxim.nfchelper.home_screen.HomeViewModel` →
  `app/src/test/java/com/maxim/nfchelper/home_screen/HomeViewModelTest.kt`.
- Класс: `<ИмяТестируемогоКласса>Test`.
- Методы: backtick-имена на английском, описывающие поведение —
  `` fun `ui state toggles when user taps write tab`() ``.
- Один тест — одно поведение; assert на конкретное ожидание, не «не упало».

## Шаблон

```kotlin
package com.maxim.nfchelper.home_screen

import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun `initial ui state is default`() {
        val viewModel = HomeViewModel()
        assertEquals(HomeUiState(), viewModel.uiState.value)
    }
}
```

## Перед завершением задачи

`./gradlew :app:testDebugUnitTest` должен быть зелёным; если тест добавлен — он выполняется
в общем прогоне, отдельного шага не требуется.
