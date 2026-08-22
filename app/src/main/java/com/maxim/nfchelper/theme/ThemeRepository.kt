package com.maxim.nfchelper.theme

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.themeDataStore by preferencesDataStore(name = "theme_settings")

object ThemeRepository {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)

    val themeMode: StateFlow<ThemeMode>
        field = _themeMode

    private val _isLoaded = MutableStateFlow(value = false)

    val isLoaded: StateFlow<Boolean>
        field = _isLoaded

    private var appContext: Context? = null

    fun init(context: Context) {
        if (appContext != null) return
        appContext = context.applicationContext
        initInternal(
            dataFlowProvider = { requireNotNull(appContext).themeDataStore.data },
            scope = CoroutineScope(Dispatchers.IO),
        )
    }

    private var loadJob: Job? = null

    @VisibleForTesting
    fun initInternal(dataFlowProvider: () -> Flow<Preferences>, scope: CoroutineScope) {
        if (loadJob?.isActive == true) return
        loadJob = scope.launch {
            dataFlowProvider()
                .map { preferences -> ThemeMode.fromName(preferences[THEME_MODE_KEY]) }
                .collect { mode ->
                    _themeMode.value = mode
                    _isLoaded.value = true
                }
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        appContext?.themeDataStore?.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
    }

    @VisibleForTesting
    fun setThemeModeForTesting(mode: ThemeMode) {
        _themeMode.value = mode
    }

    @VisibleForTesting
    fun resetForTesting() {
        appContext = null
        loadJob?.cancel()
        loadJob = null
        _themeMode.value = ThemeMode.SYSTEM
        _isLoaded.value = false
    }

    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
}
