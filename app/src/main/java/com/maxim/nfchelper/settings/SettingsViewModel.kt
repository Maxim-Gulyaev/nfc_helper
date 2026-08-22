package com.maxim.nfchelper.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxim.nfchelper.theme.ThemeMode
import com.maxim.nfchelper.theme.ThemeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    val uiState: StateFlow<SettingsUiState> =
        ThemeRepository.themeMode
            .map { mode -> SettingsUiState(themeMode = mode) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = SettingsUiState(ThemeRepository.themeMode.value),
            )

    fun onThemeModeSelected(mode: ThemeMode) {
        viewModelScope.launch {
            ThemeRepository.setThemeMode(mode)
        }
    }
}

data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)
