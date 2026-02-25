package com.zqlq.composewan.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.zqlq.composewan.R
import com.zqlq.composewan.data.model.ArticleItem
import com.zqlq.composewan.data.model.BannerItem
import com.zqlq.composewan.state.home.HomeEffect
import com.zqlq.composewan.state.home.HomeIntent
import com.zqlq.composewan.state.home.HomeState
import com.zqlq.composewan.ui.components.ArticleItemView
import com.zqlq.composewan.viewmodel.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * 首页屏幕
 * MVI 架构：State + Intent + Effect
 *
 * @param modifier 修饰符
 * @param onNavigateToWebView 跳转 WebView 回调
 * @param onNavigateToSearch 跳转搜索页回调
 * @param viewModel ViewModel
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToWebView: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HomeEffect.NavigateToWebView -> onNavigateToWebView(effect.url)
                is HomeEffect.NavigateToSearch -> onNavigateToSearch()
            }
        }
    }

    HomeContent(
        state = state,
        onIntent = viewModel::processIntent,
        modifier = modifier
    )
}

/**
 * 首页内容
 */
@Composable
private fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            onClick = { onIntent(HomeIntent.SearchClick) }
        )

        when {
            state.isLoading && state.banners.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.error != null && state.banners.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                val pullToRefreshState = rememberPullToRefreshState()
                
                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = state.isRefreshing,
                    onRefresh = { onIntent(HomeIntent.Refresh) },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    indicator = {
                        Indicator(
                            modifier = Modifier.align(Alignment.TopCenter),
                            isRefreshing = state.isRefreshing,
                            color = MaterialTheme.colorScheme.primary,
                            state = pullToRefreshState
                        )
                    }
                ) {
                    ArticleList(
                        banners = state.banners,
                        items = state.articles,
                        isLoading = state.isLoading,
                        hasMore = state.hasMore,
                        onBannerClick = { url -> onIntent(HomeIntent.BannerClick(url)) },
                        onArticleClick = { url -> onIntent(HomeIntent.ArticleClick(url)) },
                        onCollectClick = { id, isCollect -> onIntent(HomeIntent.CollectClick(id, isCollect)) },
                        onLoadMore = { onIntent(HomeIntent.LoadMore) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

/**
 * 搜索框样式组件
 */
@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = stringResource(R.string.search_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

/**
 * Banner 轮播组件
 * 支持自动轮播和手动滑动，实现无缝无限循环
 */
@Composable
private fun BannerPager(
    items: List<BannerItem>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    autoScrollInterval: Long = 3000L // 自动轮播间隔，默认3秒
) {
    val itemCount = items.size
    if (itemCount <= 1) {
        // 只有一张或没有图片时，直接显示
        items.firstOrNull()?.let { banner ->
            Box(modifier = modifier.clip(RoundedCornerShape(12.dp))) {
                Card(
                    modifier = Modifier.fillMaxSize(),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { onBannerClick(banner.url) }
                ) {
                    AsyncImage(
                        model = banner.imageUrl,
                        contentDescription = banner.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
        return
    }

    // 使用 Int.MAX_VALUE 实现伪无限循环
    val pagerState = rememberPagerState(
        initialPage = Int.MAX_VALUE / 2,
        pageCount = { Int.MAX_VALUE }
    )
    val coroutineScope = rememberCoroutineScope()
    
    // 自动轮播逻辑
    LaunchedEffect(Unit) {
        while (true) {
            delay(autoScrollInterval)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.clip(RoundedCornerShape(12.dp)),
            userScrollEnabled = true, // 允许手动滑动
            pageSpacing = 6.dp // 轮播图之间的间距
        ) {
            // 取模获取实际图片索引
            val actualIndex = it % itemCount
            val banner = items[actualIndex]
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                onClick = { onBannerClick(banner.url) }
            ) {
                AsyncImage(
                    model = banner.imageUrl,
                    contentDescription = banner.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // 华丽的指示器
        val currentActualPage = pagerState.currentPage % itemCount
        BannerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            pageCount = itemCount,
            currentPage = currentActualPage
        )
    }
}

/**
 * 华丽的Banner指示器
 * @param pageCount 总页数
 * @param currentPage 当前页码
 */
@Composable
private fun BannerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { pageIndex ->
            val isSelected = pageIndex == currentPage
            
            Box(
                modifier = Modifier
                    .background(
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
            ) {
                Spacer(
                    modifier = Modifier
                        .height(6.dp)
                        .width(if (isSelected) 24.dp else 6.dp)
                )
            }
        }
    }
}

/**
 * 文章列表组件
 */
@Composable
private fun ArticleList(
    banners: List<BannerItem>,
    items: List<ArticleItem>,
    isLoading: Boolean,
    hasMore: Boolean,
    onBannerClick: (String) -> Unit,
    onArticleClick: (String) -> Unit,
    onCollectClick: (Int, Boolean) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, items.size, hasMore, isLoading) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index
            val totalItems = layoutInfo.totalItemsCount
            lastVisibleItem != null && totalItems > 0 && lastVisibleItem >= totalItems - 3
        }.collect { shouldLoadMore ->
            if (shouldLoadMore && hasMore && !isLoading) {
                onLoadMore()
            }
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        // 将Banner作为列表的第一个item
        item {
            BannerPager(
                items = banners,
                onBannerClick = onBannerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // 文章列表
        items(items, key = { it.id }) { article ->
            ArticleItemView(
                article = article,
                onClick = { onArticleClick(article.url) },
                onCollectClick = { onCollectClick(article.id, article.isCollect) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
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


