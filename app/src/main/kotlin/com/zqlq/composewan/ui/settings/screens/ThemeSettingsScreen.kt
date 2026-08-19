package com.zqlq.composewan.ui.settings.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.settings.viewmodel.SettingsViewModel
import com.zqlq.composewan.ui.settings.viewmodel.contract.SettingsIntent
import org.koin.androidx.compose.koinViewModel

/** 换肤设置子页：浅深色模式 + 主题色。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { onBack() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_theme_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.settings_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(),
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            SettingsSection(title = stringResource(R.string.settings_theme_mode)) {
                SettingRadioRow(
                    label = stringResource(R.string.settings_theme_system),
                    selected = state.themeMode == ThemeMode.SYSTEM,
                    onClick = { viewModel.handleIntent(SettingsIntent.SelectThemeMode(ThemeMode.SYSTEM)) },
                )
                SettingRadioRow(
                    label = stringResource(R.string.settings_theme_light),
                    selected = state.themeMode == ThemeMode.LIGHT,
                    onClick = { viewModel.handleIntent(SettingsIntent.SelectThemeMode(ThemeMode.LIGHT)) },
                )
                SettingRadioRow(
                    label = stringResource(R.string.settings_theme_dark),
                    selected = state.themeMode == ThemeMode.DARK,
                    onClick = { viewModel.handleIntent(SettingsIntent.SelectThemeMode(ThemeMode.DARK)) },
                    showDivider = false,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SettingsSection(title = stringResource(R.string.settings_theme_skin)) {
                ThemeSkinOptions(
                    selected = state.themeSkin,
                    onSelect = { viewModel.handleIntent(SettingsIntent.SelectThemeSkin(it)) },
                )
            }
        }
    }
}
