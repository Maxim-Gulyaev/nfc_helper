package com.maxim.nfchelper.home_screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.maxim.nfchelper.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateSettings: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState.value,
        navigateSettings = navigateSettings,
    )
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

@Composable
private fun HomeScreenContent(
    uiState: HomeUiState,
    navigateSettings: () -> Unit,
) {
    Scaffold(
        topBar = { MainScreenAppBar(navigateSettings) },
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        val tabs = TabItem.entries

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val pagerState = rememberPagerState(pageCount = { tabs.size })

            PrimaryTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(pagerState.currentPage, matchContentSize = true),

                    )
                }
            ) {
                tabs.forEachIndexed { index, item ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            if (pagerState.currentPage != index) {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                        },
                        text = {
                            Text(
                                text = stringResource(item.title),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (tabs[page]) {
                    TabItem.READ -> ReadTabContent()
                    TabItem.WRITE -> WriteTabContent()
                }
            }

        }
    }
}

private enum class TabItem(@StringRes val title: Int) {
    READ(R.string.read_home_screen_pager_title),
    WRITE(R.string.write_home_screen_pager_title)
}