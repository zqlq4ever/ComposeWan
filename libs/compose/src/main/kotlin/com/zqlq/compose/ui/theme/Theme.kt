package com.zqlq.compose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.zqlq.common.utils.prefs.ThemeMode
import com.zqlq.common.utils.prefs.ThemeSkin

/** 紫色浅色方案。 */
private val PurpleLight =
    lightColorScheme(
        primary = Purple40,
        secondary = PurpleGrey40,
        tertiary = Pink40,
    )

/** 紫色深色方案。 */
private val PurpleDark =
    darkColorScheme(
        primary = Purple80,
        secondary = PurpleGrey80,
        tertiary = Pink80,
    )

/** 蓝色浅色方案。 */
private val BlueLight =
    lightColorScheme(
        primary = Color(0xFF1565C0),
        secondary = Color(0xFF546E7A),
        tertiary = Color(0xFF00838F),
    )

/** 蓝色深色方案。 */
private val BlueDark =
    darkColorScheme(
        primary = Color(0xFF90CAF9),
        secondary = Color(0xFFB0BEC5),
        tertiary = Color(0xFF80DEEA),
    )

/** 绿色浅色方案。 */
private val GreenLight =
    lightColorScheme(
        primary = Color(0xFF2E7D32),
        secondary = Color(0xFF558B2F),
        tertiary = Color(0xFF00695C),
    )

/** 绿色深色方案。 */
private val GreenDark =
    darkColorScheme(
        primary = Color(0xFFA5D6A7),
        secondary = Color(0xFFC5E1A5),
        tertiary = Color(0xFF80CBC4),
    )

/** 橙色浅色方案。 */
private val OrangeLight =
    lightColorScheme(
        primary = Color(0xFFEF6C00),
        secondary = Color(0xFFF57C00),
        tertiary = Color(0xFFE65100),
    )

/** 橙色深色方案。 */
private val OrangeDark =
    darkColorScheme(
        primary = Color(0xFFFFB74D),
        secondary = Color(0xFFFFCC80),
        tertiary = Color(0xFFFFAB40),
    )

/** 按换肤取浅/深色板。 */
fun colorSchemeFor(
    skin: ThemeSkin,
    darkTheme: Boolean,
): ColorScheme =
    when (skin) {
        ThemeSkin.PURPLE -> if (darkTheme) PurpleDark else PurpleLight
        ThemeSkin.BLUE -> if (darkTheme) BlueDark else BlueLight
        ThemeSkin.GREEN -> if (darkTheme) GreenDark else GreenLight
        ThemeSkin.ORANGE -> if (darkTheme) OrangeDark else OrangeLight
    }

/** 主题模式是否应使用深色。 */
@Composable
fun resolveDarkTheme(themeMode: ThemeMode): Boolean =
    when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

/**
 * 应用主题：支持主色换肤与浅深色模式。
 *
 * @param themeMode 浅深色模式
 * @param themeSkin 主色换肤
 * @param content 主题包裹的内容
 */
@Composable
fun ComposeWanTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    themeSkin: ThemeSkin = ThemeSkin.PURPLE,
    content: @Composable () -> Unit,
) {
    val darkTheme = resolveDarkTheme(themeMode)
    MaterialTheme(
        colorScheme = colorSchemeFor(themeSkin, darkTheme),
        typography = Typography,
        content = content,
    )
}
