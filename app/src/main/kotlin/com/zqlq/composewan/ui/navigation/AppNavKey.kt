package com.zqlq.composewan.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.zqlq.composewan.data.model.SystemChild
import kotlinx.serialization.Serializable

/**
 * 底部四个 Tab
 */
sealed interface TopLevelTab : NavKey

@Serializable
data object HomeTab : TopLevelTab

@Serializable
data object HotTab : TopLevelTab

@Serializable
data object SystemTab : TopLevelTab

@Serializable
data object MineTab : TopLevelTab

@Serializable
data class SearchKey(
    val query: String = "",
) : NavKey

@Serializable
data object LoginKey : NavKey

@Serializable
data object RegisterKey : NavKey

@Serializable
data object CollectKey : NavKey

@Serializable
data object AboutKey : NavKey

@Serializable
data object SettingsKey : NavKey

@Serializable
data object LanguageSettingsKey : NavKey

@Serializable
data object ThemeSettingsKey : NavKey

@Serializable
data object AccountSettingsKey : NavKey

@Serializable
data class WebViewKey(
    val url: String,
) : NavKey

@Serializable
data class SystemDetailKey(
    val categoryName: String,
    val children: List<SystemChild>,
) : NavKey
