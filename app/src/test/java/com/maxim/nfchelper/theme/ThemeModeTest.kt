package com.maxim.nfchelper.theme

import org.junit.Assert.assertEquals
import org.junit.Test

class ThemeModeTest {

    @Test
    fun `fromName returns SYSTEM for null`() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromName(null))
    }

    @Test
    fun `fromName returns SYSTEM for unknown value`() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromName("invalid"))
    }

    @Test
    fun `fromName parses each stored value`() {
        assertEquals(ThemeMode.SYSTEM, ThemeMode.fromName(ThemeMode.SYSTEM.name))
        assertEquals(ThemeMode.LIGHT, ThemeMode.fromName(ThemeMode.LIGHT.name))
        assertEquals(ThemeMode.DARK, ThemeMode.fromName(ThemeMode.DARK.name))
    }

    @Test
    fun `system mode follows system setting`() {
        assertEquals(true, isDarkTheme(ThemeMode.SYSTEM, isSystemInDarkTheme = true))
        assertEquals(false, isDarkTheme(ThemeMode.SYSTEM, isSystemInDarkTheme = false))
    }

    @Test
    fun `light and dark modes ignore system setting`() {
        assertEquals(false, isDarkTheme(ThemeMode.LIGHT, isSystemInDarkTheme = true))
        assertEquals(false, isDarkTheme(ThemeMode.LIGHT, isSystemInDarkTheme = false))
        assertEquals(true, isDarkTheme(ThemeMode.DARK, isSystemInDarkTheme = false))
        assertEquals(true, isDarkTheme(ThemeMode.DARK, isSystemInDarkTheme = true))
    }
}
