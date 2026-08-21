package com.maxim.nfchelper.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private val Context.themeDataStore by preferencesDataStore(name = "theme_settings")

object ThemeRepository {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)

    val themeMode: StateFlow<ThemeMode>
        field = _themeMode

    private val THEME_MODE_KEY = stringPreferencesKey("theme_mode")

    fun init(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            context.themeDataStore.data
                .map { preferences -> ThemeMode.fromName(preferences[THEME_MODE_KEY]) }
                .collect { mode -> _themeMode.value = mode }
        }
    }

    suspend fun setThemeMode(context: Context, mode: ThemeMode) {
        context.themeDataStore.edit { preferences ->
            preferences[THEME_MODE_KEY] = mode.name
        }
        _themeMode.value = mode
    }
}
