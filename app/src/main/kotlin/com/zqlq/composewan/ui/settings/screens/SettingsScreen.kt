package com.zqlq.composewan.ui.settings.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.zqlq.common.utils.prefs.AppLanguage
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.common.utils.prefs.ThemeSkin
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.settings.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * 设置首页：进入语言 / 换肤 / 账号子页。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageClick: () -> Unit,
    onThemeClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { onBack() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
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
            SettingsSection {
                SettingsMenuRow(
                    title = stringResource(R.string.settings_section_language),
                    subtitle = languageSummary(state.language),
                    onClick = onLanguageClick,
                )
                HorizontalDivider()
                SettingsMenuRow(
                    title = stringResource(R.string.settings_section_theme),
                    subtitle = themeSummary(state.themeMode, state.themeSkin),
                    onClick = onThemeClick,
                )
                HorizontalDivider()
                SettingsMenuRow(
                    title = stringResource(R.string.settings_section_account),
                    subtitle =
                        if (state.isLoggedIn) {
                            state.username
                        } else {
                            stringResource(R.string.not_logged_in)
                        },
                    onClick = onAccountClick,
                )
            }
        }
    }
}

@Composable
private fun languageSummary(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM -> stringResource(R.string.settings_language_system)
        AppLanguage.ZH -> stringResource(R.string.settings_language_zh)
        AppLanguage.EN -> stringResource(R.string.settings_language_en)
    }

@Composable
private fun themeSummary(
    mode: ThemeMode,
    skin: ThemeSkin,
): String {
    val modeText =
        when (mode) {
            ThemeMode.SYSTEM -> stringResource(R.string.settings_theme_system)
            ThemeMode.LIGHT -> stringResource(R.string.settings_theme_light)
            ThemeMode.DARK -> stringResource(R.string.settings_theme_dark)
        }
    return "$modeText · ${skinLabel(skin)}"
}
