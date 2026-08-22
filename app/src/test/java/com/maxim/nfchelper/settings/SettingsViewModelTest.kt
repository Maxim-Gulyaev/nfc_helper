package com.maxim.nfchelper.settings

import com.maxim.nfchelper.theme.ThemeMode
import com.maxim.nfchelper.theme.ThemeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    // Unconfined: viewModelScope (Main.immediate) starts stateIn upstream eagerly on subscribe.
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        ThemeRepository.resetForTesting()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState follows repository value`() = runTest(testDispatcher) {
        ThemeRepository.setThemeModeForTesting(ThemeMode.DARK)
        val viewModel = SettingsViewModel()

        advanceUntilIdle()

        assertEquals(ThemeMode.DARK, viewModel.uiState.value.themeMode)
    }

    @Test
    fun `uiState does not go stale when repository changes after creation`() =
        runTest(testDispatcher) {
            val viewModel = SettingsViewModel()
            backgroundScope.launch { viewModel.uiState.collect {} }

            ThemeRepository.setThemeModeForTesting(ThemeMode.LIGHT)

            advanceUntilIdle()

            assertEquals(ThemeMode.LIGHT, viewModel.uiState.value.themeMode)
        }

    @Test
    fun `onThemeModeSelected updates uiState without context`() = runTest(testDispatcher) {
        val viewModel = SettingsViewModel()
        backgroundScope.launch { viewModel.uiState.collect {} }

        viewModel.onThemeModeSelected(ThemeMode.DARK)

        advanceUntilIdle()

        assertEquals(ThemeMode.DARK, viewModel.uiState.value.themeMode)
    }
}
