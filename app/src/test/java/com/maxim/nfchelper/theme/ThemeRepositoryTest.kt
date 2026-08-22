package com.maxim.nfchelper.theme

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ThemeRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val themeKey = stringPreferencesKey("theme_mode")

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
    fun `init is idempotent and does not duplicate collectors`() = runTest(testDispatcher) {
        var subscriptions = 0
        val dataFlow: Flow<Preferences> = flowOf(emptyPreferences())
        ThemeRepository.initInternal(
            dataFlowProvider = { subscriptions++; dataFlow },
            scope = backgroundScope,
        )
        ThemeRepository.initInternal(
            dataFlowProvider = { subscriptions++; dataFlow },
            scope = backgroundScope,
        )

        runCurrent()

        assertEquals(1, subscriptions)
        assertEquals(ThemeMode.SYSTEM, ThemeRepository.themeMode.value)
        assertEquals(true, ThemeRepository.isLoaded.value)
    }

    @Test
    fun `init maps persisted value into themeMode`() = runTest(testDispatcher) {
        val preferences = emptyPreferences().toMutablePreferences().apply {
            this[themeKey] = ThemeMode.DARK.name
        }
        ThemeRepository.initInternal(
            dataFlowProvider = { flowOf(preferences.toPreferences()) },
            scope = backgroundScope,
        )

        runCurrent()

        assertEquals(ThemeMode.DARK, ThemeRepository.themeMode.value)
        assertEquals(true, ThemeRepository.isLoaded.value)
    }

    @Test
    fun `init with unknown stored value falls back to SYSTEM`() = runTest(testDispatcher) {
        val preferences = emptyPreferences().toMutablePreferences().apply {
            this[themeKey] = "bogus"
        }
        ThemeRepository.initInternal(
            dataFlowProvider = { flowOf(preferences.toPreferences()) },
            scope = backgroundScope,
        )

        runCurrent()

        assertEquals(ThemeMode.SYSTEM, ThemeRepository.themeMode.value)
        assertEquals(true, ThemeRepository.isLoaded.value)
    }

    @Test
    fun `setThemeMode updates state immediately`() = runTest(testDispatcher) {
        ThemeRepository.setThemeModeForTesting(ThemeMode.LIGHT)

        assertEquals(ThemeMode.LIGHT, ThemeRepository.themeMode.value)
    }
}
