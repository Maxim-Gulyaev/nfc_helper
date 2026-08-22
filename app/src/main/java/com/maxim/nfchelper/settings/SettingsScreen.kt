package com.maxim.nfchelper.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maxim.nfchelper.R
import com.maxim.nfchelper.theme.ThemeMode

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    Scaffold(
        topBar = { SettingsScreenAppBar(navigateBack) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            ThemeSettingsContent(
                selectedMode = uiState.value.themeMode,
                onThemeModeSelected = viewModel::onThemeModeSelected,
            )
        }
    }
}

@Composable
private fun ThemeSettingsContent(
    selectedMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.title_theme_settings_screen),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp),
        )
        ThemeOptionRow(
            titleRes = R.string.title_theme_mode_system,
            mode = ThemeMode.SYSTEM,
            selectedMode = selectedMode,
            onThemeModeSelected = onThemeModeSelected,
        )
        ThemeOptionRow(
            titleRes = R.string.title_theme_mode_light,
            mode = ThemeMode.LIGHT,
            selectedMode = selectedMode,
            onThemeModeSelected = onThemeModeSelected,
        )
        ThemeOptionRow(
            titleRes = R.string.title_theme_mode_dark,
            mode = ThemeMode.DARK,
            selectedMode = selectedMode,
            onThemeModeSelected = onThemeModeSelected,
        )
    }
}

@Composable
private fun ThemeOptionRow(
    titleRes: Int,
    mode: ThemeMode,
    selectedMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onThemeModeSelected(mode) },
    ) {
        RadioButton(
            selected = selectedMode == mode,
            onClick = null,
        )
        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreenAppBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(R.string.title_settings_screen))
        },
        navigationIcon = {
            IconButton(
                onClick = { onBackClick() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_top_app_bar),
                    contentDescription = null,
                )
            }
        }
    )
}
