# Skill: Unit Testing (local unit tests only)

Rules for writing and running **unit** tests (`app/src/test/`). Instrumentation tests
(`app/src/androidTest/`) are out of scope for this skill.

## Running

```bash
./gradlew :app:testDebugUnitTest              # all unit tests
./gradlew :app:testDebugUnitTest --tests "com.maxim.nfchelper.home_screen.HomeViewModelTest"
```

Report: `app/build/reports/tests/testDebugUnitTest/index.html`.

## Current infrastructure

- Only **JUnit4** is connected (`testImplementation(libs.junit)`).
- NOT connected: `kotlinx-coroutines-test`, `turbine`, `mockk`/`mockito`, Robolectric.
- Existing tests: the template `ExampleUnitTest.kt` (can be considered a JUnit4 style example).

## What we test

| Code | Test it? | How |
|---|---|---|
| ViewModel / UiState logic (pure state transitions) | yes | JUnit4, no Android dependencies |
| Pure functions/utilities (parsing, formatting, validation) | yes | plain JUnit4 |
| Composable UI | not with unit tests | out of scope; build/manual verification only |
| Code with `Context`, NFC adapter, Android APIs | not directly | extract logic into pure Kotlin and test that |

If a test needs coroutines/mocks — **first agree with the user** on adding dependencies
(`kotlinx-coroutines-test`, `turbine`, `mockk`) to `app/build.gradle.kts`.
Until agreed, write tests that plain JUnit4 suffices for (e.g., check
`MutableStateFlow.value` directly without `viewModelScope`).

## Location and naming

- Mirror the package: class `com.maxim.nfchelper.home_screen.HomeViewModel` →
  `app/src/test/java/com/maxim/nfchelper/home_screen/HomeViewModelTest.kt`.
- Class: `<NameOfTestedClass>Test`.
- Methods: backtick names in English describing the behavior —
  `` fun `ui state toggles when user taps write tab`() ``.
- One test = one behavior; assert a concrete expectation, not just "didn't crash".

## Template

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

## Before finishing a task

`./gradlew :app:testDebugUnitTest` must be green; if a test was added, it runs as part of
the overall run — no separate step is required.