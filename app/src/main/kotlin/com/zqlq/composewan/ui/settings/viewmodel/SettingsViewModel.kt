package com.zqlq.composewan.ui.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqlq.common.utils.prefs.AppLanguage
import com.zqlq.common.utils.prefs.AppPreferences
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.common.utils.prefs.ThemeSkin
import com.zqlq.common.utils.session.UserSession
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.settings.LocaleHelper
import com.zqlq.composewan.ui.settings.usecase.SettingsUseCase
import com.zqlq.composewan.ui.settings.viewmodel.contract.SettingsEvent
import com.zqlq.composewan.ui.settings.viewmodel.contract.SettingsIntent
import com.zqlq.composewan.ui.settings.viewmodel.contract.SettingsUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** 设置页 ViewModel。 */
class SettingsViewModel(
    private val useCase: SettingsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _events = Channel<SettingsEvent>(Channel.BUFFERED)
    val events: Flow<SettingsEvent> = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            combine(
                AppPreferences.languageFlow,
                AppPreferences.themeModeFlow,
                AppPreferences.themeSkinFlow,
                UserSession.isLoggedInFlow,
                UserSession.usernameFlow,
            ) { language, themeMode, themeSkin, isLoggedIn, username ->
                SettingsUiState(
                    language = language,
                    themeMode = themeMode,
                    themeSkin = themeSkin,
                    isLoggedIn = isLoggedIn,
                    username = username,
                    isLoggingOut = _uiState.value.isLoggingOut,
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.SelectLanguage -> selectLanguage(intent.language)
            is SettingsIntent.SelectThemeMode -> selectThemeMode(intent.mode)
            is SettingsIntent.SelectThemeSkin -> selectThemeSkin(intent.skin)
            SettingsIntent.LoginClick -> navigateToLogin()
            SettingsIntent.LogoutClick -> logout()
        }
    }

    private fun selectLanguage(language: AppLanguage) {
        LocaleHelper.setAndApply(language)
    }

    private fun selectThemeMode(mode: ThemeMode) {
        AppPreferences.setThemeMode(mode)
    }

    private fun selectThemeSkin(skin: ThemeSkin) {
        AppPreferences.setThemeSkin(skin)
    }

    private fun navigateToLogin() {
        viewModelScope.launch { _events.send(SettingsEvent.NavigateToLogin) }
    }

    private fun logout() {
        if (_uiState.value.isLoggingOut) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoggingOut = true) }
            useCase
                .logout()
                .onSuccess {
                    _uiState.update { it.copy(isLoggingOut = false) }
                    _events.send(SettingsEvent.LogoutSuccess)
                    _events.send(SettingsEvent.ShowMessageRes(R.string.logout_success))
                }.onFailure { e ->
                    _uiState.update { it.copy(isLoggingOut = false) }
                    val message = e.message.orEmpty()
                    if (message.isNotBlank()) {
                        _events.send(SettingsEvent.ShowMessage(message))
                    } else {
                        _events.send(SettingsEvent.ShowMessageRes(R.string.logout_failed))
                    }
                }
        }
    }
}
