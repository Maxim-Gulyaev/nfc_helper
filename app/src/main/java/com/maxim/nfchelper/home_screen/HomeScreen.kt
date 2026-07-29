package com.maxim.nfchelper.home_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.maxim.nfchelper.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateSettings: () -> Unit,
) {
    Scaffold(
        topBar = { MainScreenAppBar(navigateSettings) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) { }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenAppBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(stringResource(R.string.title_main_screen))
        },
        actions = {
            IconButton(onClick = { onSettingsClick() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_settings_main_screen),
                    contentDescription = "Localized description"
                )
            }
        }
    )
}