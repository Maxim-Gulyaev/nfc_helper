package com.maxim.nfchelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import com.maxim.nfchelper.navigation.NfcHelperApp
import com.maxim.nfchelper.theme.ThemeRepository
import com.maxim.nfchelper.theme.isDarkTheme
import com.maxim.nfchelper.ui.theme.NFCHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ThemeRepository.init(this)
        setContent {
            val themeMode = ThemeRepository.themeMode.collectAsState()
            NFCHelperTheme(
                darkTheme = isDarkTheme(themeMode.value, isSystemInDarkTheme()),
            ) {
                NfcHelperApp()
            }
        }
    }
}
