package com.maxim.nfchelper.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.maxim.nfchelper.home_screen.HomeScreen
import com.maxim.nfchelper.navigation.Screen.Home
import com.maxim.nfchelper.navigation.Screen.Settings
import com.maxim.nfchelper.settings.SettingsScreen

@Composable
fun NfcHelperApp() {
    val backStack = rememberNavBackStack(Home)

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Home> {
                HomeScreen(
                    navigateSettings = { backStack.add(Settings) }
                )
            }
            entry<Settings> {
                SettingsScreen()
            }
        }
    )
}