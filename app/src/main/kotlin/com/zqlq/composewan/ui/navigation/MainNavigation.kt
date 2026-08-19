package com.zqlq.composewan.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.zqlq.composewan.R
import com.zqlq.composewan.ui.screens.about.AboutScreen
import com.zqlq.composewan.ui.screens.collect.CollectScreen
import com.zqlq.composewan.ui.screens.home.HomeScreen
import com.zqlq.composewan.ui.screens.hot.HotScreen
import com.zqlq.composewan.ui.screens.login.LoginScreen
import com.zqlq.composewan.ui.screens.login.RegisterScreen
import com.zqlq.composewan.ui.screens.mine.MineScreen
import com.zqlq.composewan.ui.screens.search.SearchScreen
import com.zqlq.composewan.ui.screens.system.SystemDetailScreen
import com.zqlq.composewan.ui.screens.system.SystemScreen
import com.zqlq.composewan.ui.screens.webview.WebViewScreen

/**
 * 底栏导航项
 */
private data class TabItem(
    val key: TopLevelTab,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

/**
 * 主导航：Navigation3 多 back stack + 底栏
 */
@Composable
fun MainNavigation() {
    val topLevelBackStack = remember { TopLevelBackStack<NavKey>(HomeTab) }
    val currentKey = topLevelBackStack.backStack.lastOrNull()
    val showBottomBar = currentKey is TopLevelTab

    val tabItems =
        listOf(
            TabItem(HomeTab, R.string.nav_home, Icons.Filled.Home, Icons.Outlined.Home),
            TabItem(HotTab, R.string.nav_hot, Icons.Filled.Whatshot, Icons.Outlined.Whatshot),
            TabItem(SystemTab, R.string.nav_system, Icons.Filled.Category, Icons.Outlined.Category),
            TabItem(MineTab, R.string.nav_mine, Icons.Filled.Person, Icons.Outlined.Person),
        )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // 状态栏交给各页自己处理，避免二级页 TopAppBar 再垫一层
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    tabItems.forEach { item ->
                        val selected = item.key == topLevelBackStack.topLevelKey
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = stringResource(item.labelResId),
                                )
                            },
                            label = { Text(stringResource(item.labelResId)) },
                            selected = selected,
                            onClick = { topLevelBackStack.addTopLevel(item.key) },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding()),
            onBack = { topLevelBackStack.removeLast() },
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider =
                entryProvider {
                    entry<HomeTab> {
                        HomeScreen(
                            modifier = Modifier.statusBarsPadding(),
                            onNavigateToWebView = { url -> topLevelBackStack.add(WebViewKey(url)) },
                            onNavigateToSearch = { topLevelBackStack.add(SearchKey()) },
                        )
                    }
                    entry<HotTab> {
                        HotScreen(
                            modifier = Modifier.statusBarsPadding(),
                            onNavigateToWebView = { url -> topLevelBackStack.add(WebViewKey(url)) },
                            onNavigateToSearch = { query -> topLevelBackStack.add(SearchKey(query)) },
                        )
                    }
                    entry<SystemTab> {
                        SystemScreen(
                            modifier = Modifier.statusBarsPadding(),
                            onNavigateToSystemDetail = { categoryName, children ->
                                topLevelBackStack.add(SystemDetailKey(categoryName, children))
                            },
                        )
                    }
                    entry<MineTab> {
                        MineScreen(
                            modifier = Modifier.statusBarsPadding(),
                            onAboutClick = { topLevelBackStack.add(AboutKey) },
                            onCollectClick = { topLevelBackStack.add(CollectKey) },
                            onLoginClick = { topLevelBackStack.add(LoginKey) },
                        )
                    }
                    entry<SearchKey> { key ->
                        SearchScreen(
                            onBack = { topLevelBackStack.removeLast() },
                            onNavigateToWebView = { url -> topLevelBackStack.add(WebViewKey(url)) },
                            initialQuery = key.query,
                        )
                    }
                    entry<LoginKey> {
                        LoginScreen(
                            onBack = { topLevelBackStack.removeLast() },
                            onNavigateToRegister = { topLevelBackStack.add(RegisterKey) },
                            onLoginSuccess = { topLevelBackStack.removeLast() },
                        )
                    }
                    entry<RegisterKey> {
                        RegisterScreen(
                            onBack = { topLevelBackStack.removeLast() },
                            onRegisterSuccess = { topLevelBackStack.removeLast() },
                        )
                    }
                    entry<CollectKey> {
                        CollectScreen(
                            onBack = { topLevelBackStack.removeLast() },
                            onNavigateToWebView = { url -> topLevelBackStack.add(WebViewKey(url)) },
                        )
                    }
                    entry<AboutKey> {
                        AboutScreen(onBack = { topLevelBackStack.removeLast() })
                    }
                    entry<WebViewKey> { key ->
                        WebViewScreen(
                            url = key.url,
                            onBack = { topLevelBackStack.removeLast() },
                        )
                    }
                    entry<SystemDetailKey> { key ->
                        SystemDetailScreen(
                            onBack = { topLevelBackStack.removeLast() },
                            onNavigateToWebView = { url -> topLevelBackStack.add(WebViewKey(url)) },
                            categoryName = key.categoryName,
                            children = key.children,
                        )
                    }
                },
        )
    }
}
