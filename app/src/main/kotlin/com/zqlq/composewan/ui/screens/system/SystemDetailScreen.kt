package com.zqlq.composewan.ui.screens.system

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.state.system.SystemDetailEffect
import com.zqlq.composewan.state.system.SystemDetailIntent
import com.zqlq.composewan.ui.components.ArticleItemView
import com.zqlq.composewan.viewmodel.system.SystemDetailViewModel
import com.zqlq.composewan.viewmodel.system.SystemDetailViewModelFactory
import kotlinx.coroutines.flow.collectLatest

/**
 * 体系详情页面
 *
 * @param modifier 修饰符
 * @param onBack 返回回调
 * @param onNavigateToWebView 跳转 WebView 回调
 * @param categoryName 分类名称
 * @param children 子分类列表
 * @param viewModel ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemDetailScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onNavigateToWebView: (String) -> Unit = {},
    categoryName: String,
    children: List<SystemChild>,
    viewModel: SystemDetailViewModel = viewModel(
        factory = SystemDetailViewModelFactory(categoryName, children)
    )
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // 系统返回键处理
    BackHandler(onBack = onBack)

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SystemDetailEffect.NavigateToWebView -> onNavigateToWebView(effect.url)
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(categoryName) },
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab 栏
            PrimaryScrollableTabRow(
                selectedTabIndex = state.children.indexOfFirst { it.id == state.selectedChildId },
                modifier = Modifier.fillMaxWidth(),
                edgePadding = 4.dp,
                divider = { /* 隐藏分割线 */ }
            ) {
                state.children.forEach { child ->
                    Tab(
                        selected = child.id == state.selectedChildId,
                        onClick = { 
                            viewModel.processIntent(SystemDetailIntent.SelectChild(child.id))
                        },
                        text = {
                            Text(
                                text = child.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 内容区域
            when {
                state.isLoading && state.articles.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "未知错误",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    ArticleList(
                        articles = state.articles,
                        isLoading = state.isLoading,
                        onArticleClick = { url -> viewModel.processIntent(SystemDetailIntent.ArticleClick(url)) },
                        onCollectClick = { id, isCollect ->
                            viewModel.processIntent(SystemDetailIntent.CollectClick(id, isCollect))
                        },
                        onLoadMore = { viewModel.processIntent(SystemDetailIntent.LoadMore) }
                    )
                }
            }
        }
    }
}

/**
 * 文章列表组件
 */
@Composable
private fun ArticleList(
    articles: List<ArticleItem>,
    isLoading: Boolean,
    onArticleClick: (String) -> Unit,
    onCollectClick: (Int, Boolean) -> Unit,
    onLoadMore: () -> Unit
) {
    val listState = rememberLazyListState()

    // 监听滚动，触发加载更多
    LaunchedEffect(listState, articles.size, isLoading) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = layoutInfo.totalItemsCount
            lastVisibleItem != null && totalItems > 0 && lastVisibleItem >= totalItems - 3
        }.collect { shouldLoadMore ->
            if (shouldLoadMore && !isLoading) {
                onLoadMore()
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(articles, key = { it.id }) {
            ArticleItemView(
                article = it,
                onClick = { onArticleClick(it.url) },
                onCollectClick = { onCollectClick(it.id, it.isCollect) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}


