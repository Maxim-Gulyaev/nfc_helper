package com.maxim.nfchelper.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.nfchelper.theme.ThemeMode
import com.maxim.nfchelper.theme.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    val uiState: StateFlow<SettingsUiState>
        field = MutableStateFlow(SettingsUiState(ThemeRepository.themeMode.value))

    fun onThemeModeSelected(mode: ThemeMode) {
        uiState.value = uiState.value.copy(themeMode = mode)
        viewModelScope.launch {
            ThemeRepository.setThemeMode(getApplication(), mode)
        }
    }
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)
