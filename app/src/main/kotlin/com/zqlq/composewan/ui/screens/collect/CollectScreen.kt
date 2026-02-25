package com.zqlq.composewan.ui.screens.collect

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zqlq.composewan.ui.components.ArticleItemView
import com.zqlq.composewan.state.collect.CollectIntent
import com.zqlq.composewan.state.collect.CollectState
import com.zqlq.composewan.viewmodel.collect.CollectViewModel

/**
 * 收藏页面
 * 支持收藏文章列表展示、下拉刷新、加载更多、取消收藏
 *
 * @param onBack 返回回调
 * @param onNavigateToWebView 跳转到WebView回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectScreen(
    onBack: () -> Unit,
    onNavigateToWebView: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: CollectViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 系统返回键处理
    BackHandler {
        onBack()
    }

    // 处理副作用
    LaunchedEffect(Unit) {
        viewModel.effect.collect {effect ->
            when (effect) {
                is com.zqlq.composewan.state.collect.CollectEffect.NavigateToWebView -> {
                    onNavigateToWebView(effect.url)
                }
                is com.zqlq.composewan.state.collect.CollectEffect.ShowMessage -> {
                    // 这里可以添加消息提示，例如Toast或Snackbar
                }
            }
        }
    }

    // 下拉刷新状态
    val pullToRefreshState = rememberPullToRefreshState()
    val lazyListState = rememberLazyListState()

    // 监听滚动到底部，加载更多
    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == state.articles.size - 1) {
            viewModel.processIntent(CollectIntent.LoadMoreCollectList)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("我的收藏") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PullToRefreshBox(
                state = pullToRefreshState,
                onRefresh = { viewModel.processIntent(CollectIntent.RefreshCollectList) },
                isRefreshing = state.isRefreshing,
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 收藏文章列表
                    items(state.articles) {
                        ArticleItemView(
                            article = it,
                            onClick = { viewModel.processIntent(CollectIntent.OnArticleClick(it)) },
                            onCollectClick = { viewModel.processIntent(CollectIntent.OnUncollectClick(it)) },
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .testTag("article_item")
                        )
                    }

                    // 加载更多指示器
                    if (state.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 初始加载指示器
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
