package com.zqlq.composewan.ui.hot.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.R
import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem
import com.zqlq.composewan.ui.components.ErrorRetryPane
import com.zqlq.composewan.ui.hot.viewmodel.HotViewModel
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotEvent
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotIntent
import com.zqlq.composewan.ui.hot.viewmodel.contract.HotUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

/**
 * 热门屏幕
 * 包含搜索热词和常用网站两个模块
 *
 * @param modifier 修饰符
 * @param onNavigateToWebView 跳转 WebView 回调
 * @param onNavigateToSearch 跳转到搜索页面回调
 * @param viewModel ViewModel
 */
@Composable
fun HotScreen(
    modifier: Modifier = Modifier,
    onNavigateToWebView: (String) -> Unit = {},
    onNavigateToSearch: (String) -> Unit = {},
    viewModel: HotViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        val current = viewModel.uiState.value
        if (current.error != null && current.hotKeys.isEmpty() && !current.isLoading) {
            viewModel.handleIntent(HotIntent.LoadData)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HotEvent.ShowToast -> {
                    ToastUtils.show(event.message)
                }

                is HotEvent.NavigateToWebView -> {
                    onNavigateToWebView(event.url)
                }

                is HotEvent.NavigateToSearch -> {
                    onNavigateToSearch(event.keyword)
                }
            }
        }
    }

    HotContent(
        state = state,
        onIntent = viewModel::handleIntent,
        modifier = modifier,
    )
}

/**
 * 热门内容
 */
@Composable
private fun HotContent(
    state: HotUiState,
    onIntent: (HotIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.isLoading && state.hotKeys.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        state.error != null && state.hotKeys.isEmpty() -> {
            ErrorRetryPane(
                message = state.error,
                onRetry = { onIntent(HotIntent.LoadData) },
                modifier = modifier,
            )
        }

        else -> {
            Column(
                modifier =
                    modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
            ) {
                SectionTitle(title = stringResource(R.string.hot_section_keys))

                Spacer(modifier = Modifier.height(12.dp))

                HotKeyFlowRow(
                    items = state.hotKeys,
                    onItemClick = { name -> onIntent(HotIntent.HotKeyClick(name)) },
                )

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(title = stringResource(R.string.hot_section_websites))

                Spacer(modifier = Modifier.height(12.dp))

                WebsiteFlowRow(
                    items = state.websites,
                    onItemClick = { url -> onIntent(HotIntent.WebsiteClick(url)) },
                )
            }
        }
    }
}

/**
 * 分组标题
 */
@Composable
private fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

/**
 * 热词流式布局（自适应宽高，不可滚动）
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HotKeyFlowRow(
    items: List<HotKeyItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            HotKeyItem(
                item = item,
                onClick = { onItemClick(item.name) },
            )
        }
    }
}

/**
 * 热词项（自适应宽度）
 */
@Composable
private fun HotKeyItem(
    item: HotKeyItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
        onClick = onClick,
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        )
    }
}

/**
 * 常用网站流式布局（自适应宽高，不可滚动）
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WebsiteFlowRow(
    items: List<WebsiteItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items.forEach { item ->
            WebsiteItemView(
                item = item,
                onClick = { onItemClick(item.url) },
            )
        }
    }
}

/**
 * 网站项（自适应宽度）
 */
@Composable
private fun WebsiteItemView(
    item: WebsiteItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
        onClick = onClick,
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        )
    }
}
