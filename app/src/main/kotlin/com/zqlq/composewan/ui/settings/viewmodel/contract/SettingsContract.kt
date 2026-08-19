package com.zqlq.composewan.ui.settings.viewmodel.contract

import com.zqlq.common.utils.prefs.AppLanguage
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.common.utils.prefs.ThemeSkin

/** 设置页 UI 状态。 */
data class SettingsUiState(
    val language: AppLanguage = AppLanguage.SYSTEM,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val themeSkin: ThemeSkin = ThemeSkin.PURPLE,
    val isLoggedIn: Boolean = false,
    val username: String = "",
    val isLoggingOut: Boolean = false,
)

/** 设置页用户意图。 */
sealed interface SettingsIntent {
    data class SelectLanguage(
        val language: AppLanguage,
    ) : SettingsIntent

    data class SelectThemeMode(
        val mode: ThemeMode,
    ) : SettingsIntent

    data class SelectThemeSkin(
        val skin: ThemeSkin,
    ) : SettingsIntent

    data object LoginClick : SettingsIntent

    data object LogoutClick : SettingsIntent
}

/** 设置页一次性事件。 */
sealed interface SettingsEvent {
    data object NavigateToLogin : SettingsEvent

    data object LogoutSuccess : SettingsEvent

    data class ShowMessageRes(
        val resId: Int,
    ) : SettingsEvent

    data class ShowMessage(
        val message: String,
    ) : SettingsEvent
}
