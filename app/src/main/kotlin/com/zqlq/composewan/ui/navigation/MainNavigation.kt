package com.zqlq.composewan.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.screens.about.AboutScreen
import com.zqlq.composewan.ui.screens.collect.CollectScreen
import com.zqlq.composewan.ui.screens.home.HomeScreen
import com.zqlq.composewan.ui.screens.hot.HotScreen
import com.zqlq.composewan.ui.screens.mine.MineScreen
import com.zqlq.composewan.ui.screens.search.SearchScreen
import com.zqlq.composewan.ui.screens.system.SystemDetailScreen
import com.zqlq.composewan.ui.screens.system.SystemScreen
import com.zqlq.composewan.ui.screens.webview.WebViewScreen

/**
 * 导航项数据类
 * @param label 标签文本资源ID
 * @param selectedIcon 选中状态图标
 * @param unselectedIcon 未选中状态图标
 */
data class NavItem(
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

/**
 * 主导航组件
 * 提供底部导航栏，支持在首页、热门、体系、我的四个页面间切换
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    var selectedItem by remember { mutableIntStateOf(0) }
    var webViewUrl by remember { mutableStateOf<String?>(null) }
    var showAboutScreen by remember { mutableStateOf(false) }
    var showSearchScreen by remember { mutableStateOf(false) }
    var showSystemDetailScreen by remember { mutableStateOf(false) }
    var showCollectScreen by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf("") }
    var selectedCategoryChildren by remember { mutableStateOf(emptyList<com.zqlq.composewan.data.model.SystemChild>()) }
    var initialSearchQuery by remember { mutableStateOf("") }

    val navItems = listOf(
        NavItem(
            labelResId = R.string.nav_home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavItem(
            labelResId = R.string.nav_hot,
            selectedIcon = Icons.Filled.Whatshot,
            unselectedIcon = Icons.Outlined.Whatshot
        ),
        NavItem(
            labelResId = R.string.nav_system,
            selectedIcon = Icons.Filled.Category,
            unselectedIcon = Icons.Outlined.Category
        ),
        NavItem(
            labelResId = R.string.nav_mine,
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    // 显示AboutScreen
    if (showAboutScreen) {
        AboutScreen(
            onBack = { showAboutScreen = false },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // 显示WebViewScreen
    webViewUrl?.let { url ->
        WebViewScreen(
            url = url,
            onBack = { webViewUrl = null },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // 显示CollectScreen
    if (showCollectScreen) {
        CollectScreen(
            onBack = { showCollectScreen = false },
            onNavigateToWebView = { url -> webViewUrl = url },
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // 显示SearchScreen
    if (showSearchScreen) {
        SearchScreen(
            onBack = {
                showSearchScreen = false
                initialSearchQuery = ""
            },
            onNavigateToWebView = { url -> webViewUrl = url },
            initialQuery = initialSearchQuery,
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    // 显示SystemDetailScreen
    if (showSystemDetailScreen) {
        SystemDetailScreen(
            onBack = {
                showSystemDetailScreen = false
                selectedCategoryName = ""
                selectedCategoryChildren = emptyList()
            },
            onNavigateToWebView = { url -> webViewUrl = url },
            categoryName = selectedCategoryName,
            children = selectedCategoryChildren,
            modifier = Modifier.fillMaxSize()
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = stringResource(item.labelResId)
                            )
                        },
                        label = { Text(stringResource(item.labelResId)) },
                        selected = selectedItem == index,
                        onClick = { selectedItem = index }
                    )
                }
            }
        }
    ) {
        when (selectedItem) {
            0 -> HomeScreen(
                modifier = Modifier.padding(it),
                onNavigateToWebView = { url -> webViewUrl = url },
                onNavigateToSearch = {
                    initialSearchQuery = ""
                    showSearchScreen = true
                }
            )
            1 -> HotScreen(
                modifier = Modifier.padding(it),
                onNavigateToWebView = { url -> webViewUrl = url },
                onNavigateToSearch = { query ->
                    initialSearchQuery = query
                    showSearchScreen = true
                }
            )

            2 -> SystemScreen(
                modifier = Modifier.padding(it),
                onNavigateToSystemDetail = { categoryName, children ->
                    selectedCategoryName = categoryName
                    selectedCategoryChildren = children
                    showSystemDetailScreen = true
                }
            )
            3 -> MineScreen(
                modifier = Modifier.padding(it),
                onAboutClick = { showAboutScreen = true },
                onCollectClick = { showCollectScreen = true }
            )
        }
    }
}
