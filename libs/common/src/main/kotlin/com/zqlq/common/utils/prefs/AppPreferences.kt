package com.zqlq.common.utils.prefs

import com.zqlq.common.utils.storage.MMKVUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 应用语言偏好。 */
enum class AppLanguage(
    val value: String,
) {
    SYSTEM("system"),
    ZH("zh"),
    EN("en"),
    ;

    companion object {
        fun fromValue(value: String): AppLanguage = entries.firstOrNull { it.value == value } ?: SYSTEM
    }
}

/** 浅深色主题模式。 */
enum class ThemeMode(
    val value: String,
) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark"),
    ;

    companion object {
        fun fromValue(value: String): ThemeMode = entries.firstOrNull { it.value == value } ?: SYSTEM
    }
}

/** 主色换肤。 */
enum class ThemeSkin(
    val value: String,
) {
    PURPLE("purple"),
    BLUE("blue"),
    GREEN("green"),
    ORANGE("orange"),
    ;

    companion object {
        fun fromValue(value: String): ThemeSkin = entries.firstOrNull { it.value == value } ?: PURPLE
    }
}

/** 应用偏好：语言、主题模式、主色换肤，MMKV 持久化。 */
object AppPreferences {
    private const val KEY_LANGUAGE = "app_language"
    private const val KEY_THEME_MODE = "app_theme_mode"
    private const val KEY_THEME_SKIN = "app_theme_skin"

    private val _language = MutableStateFlow(readLanguage())
    val languageFlow: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _themeMode = MutableStateFlow(readThemeMode())
    val themeModeFlow: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _themeSkin = MutableStateFlow(readThemeSkin())
    val themeSkinFlow: StateFlow<ThemeSkin> = _themeSkin.asStateFlow()

    fun language(): AppLanguage = _language.value

    fun themeMode(): ThemeMode = _themeMode.value

    fun themeSkin(): ThemeSkin = _themeSkin.value

    fun setLanguage(language: AppLanguage) {
        MMKVUtils.putString(KEY_LANGUAGE, language.value)
        _language.value = language
    }

    fun setThemeMode(mode: ThemeMode) {
        MMKVUtils.putString(KEY_THEME_MODE, mode.value)
        _themeMode.value = mode
    }

    fun setThemeSkin(skin: ThemeSkin) {
        MMKVUtils.putString(KEY_THEME_SKIN, skin.value)
        _themeSkin.value = skin
    }

    private fun readLanguage(): AppLanguage = AppLanguage.fromValue(MMKVUtils.getString(KEY_LANGUAGE, AppLanguage.SYSTEM.value))

    private fun readThemeMode(): ThemeMode = ThemeMode.fromValue(MMKVUtils.getString(KEY_THEME_MODE, ThemeMode.SYSTEM.value))

    private fun readThemeSkin(): ThemeSkin = ThemeSkin.fromValue(MMKVUtils.getString(KEY_THEME_SKIN, ThemeSkin.PURPLE.value))
}
