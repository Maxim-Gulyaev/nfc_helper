package com.maxim.nfchelper.home_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel: ViewModel() {

    val uiState: StateFlow<HomeUiState>
        field = MutableStateFlow(HomeUiState())
}

data class HomeUiState(
    val dummy: Boolean = false,
)