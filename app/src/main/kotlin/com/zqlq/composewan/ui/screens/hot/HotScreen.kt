package com.zqlq.composewan.ui.screens.hot

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem
import com.zqlq.composewan.state.hot.HotEffect
import com.zqlq.composewan.state.hot.HotIntent
import com.zqlq.composewan.state.hot.HotState
import com.zqlq.composewan.viewmodel.hot.HotViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * 热门屏幕
 * 包含搜索热词和常用网站两个模块
 *
 * @param modifier 修饰符
 * @param onNavigateToWebView 跳转 WebView 回调
 * @param viewModel ViewModel
 */
@Composable
fun HotScreen(
    modifier: Modifier = Modifier,
    onNavigateToWebView: (String) -> Unit = {},
    viewModel: HotViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is HotEffect.ShowToast -> {
                    ToastUtils.show(effect.message)
                }
                is HotEffect.NavigateToWebView -> onNavigateToWebView(effect.url)
            }
        }
    }

    HotContent(
        state = state,
        onIntent = viewModel::processIntent,
        modifier = modifier
    )
}

/**
 * 热门内容
 */
@Composable
private fun HotContent(
    state: HotState,
    onIntent: (HotIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.hotKeys.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error != null && state.hotKeys.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error ?: "未知错误",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                SectionTitle(title = "搜索热词")

                Spacer(modifier = Modifier.height(12.dp))

                HotKeyFlowRow(
                    items = state.hotKeys,
                    onItemClick = { name -> onIntent(HotIntent.HotKeyClick(name)) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                SectionTitle(title = "常用网站")

                Spacer(modifier = Modifier.height(12.dp))

                WebsiteFlowRow(
                    items = state.websites,
                    onItemClick = { url -> onIntent(HotIntent.WebsiteClick(url)) }
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
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier
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
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            HotKeyItem(
                item = item,
                onClick = { onItemClick(item.name) }
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = onClick
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
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
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            WebsiteItemView(
                item = item,
                onClick = { onItemClick(item.url) }
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        onClick = onClick
    ) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}
