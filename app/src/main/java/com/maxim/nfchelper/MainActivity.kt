package com.maxim.nfchelper

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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
        holdFirstFrameUntilThemeLoaded()
        setContent {
            val themeMode = ThemeRepository.themeMode.collectAsState()
            NFCHelperTheme(
                darkTheme = isDarkTheme(themeMode.value, isSystemInDarkTheme()),
            ) {
                NfcHelperApp()
            }
        }
    }

    private fun holdFirstFrameUntilThemeLoaded() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (ThemeRepository.isLoaded.value) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            },
        )
    }
}
