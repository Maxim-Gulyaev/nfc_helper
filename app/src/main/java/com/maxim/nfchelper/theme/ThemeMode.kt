package com.maxim.nfchelper.theme

enum class ThemeMode {
    SYSTEM, LIGHT, DARK;

    companion object {
        fun fromName(name: String?): ThemeMode =
            entries.firstOrNull { it.name == name } ?: SYSTEM
    }
}

fun isDarkTheme(mode: ThemeMode, isSystemInDarkTheme: Boolean): Boolean = when (mode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}
