package com.maxim.nfchelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.maxim.nfchelper.main_screen.MainScreen
import com.maxim.nfchelper.ui.theme.NFCHelperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NFCHelperTheme {
                MainScreen()
            }
        }
    }
}
