package com.zqlq.composewan.ui.screens.system

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.data.model.SystemCategory
import com.zqlq.composewan.data.model.SystemChild
import com.zqlq.composewan.state.system.SystemEffect
import com.zqlq.composewan.state.system.SystemIntent
import com.zqlq.composewan.state.system.SystemState
import com.zqlq.composewan.viewmodel.system.SystemViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * 体系屏幕
 * 展示可折叠的分类列表
 *
 * @param modifier 修饰符
 * @param viewModel ViewModel
 */
@Composable
fun SystemScreen(
    modifier: Modifier = Modifier,
    viewModel: SystemViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SystemEffect.ShowToast -> {
                    ToastUtils.show(effect.message)
                }
            }
        }
    }

    SystemContent(
        state = state,
        onIntent = viewModel::processIntent,
        modifier = modifier
    )
}

/**
 * 体系内容
 */
@Composable
private fun SystemContent(
    state: SystemState,
    onIntent: (SystemIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading && state.categories.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        state.error != null && state.categories.isEmpty() -> {
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
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.categories,
                    key = { it.id }
                ) { category ->
                    CategoryItem(
                        category = category,
                        isExpanded = state.expandedIds.contains(category.id),
                        onToggleExpand = { onIntent(SystemIntent.ToggleExpand(category.id)) },
                        onChildClick = { child -> onIntent(SystemIntent.ChildClick(child.name)) }
                    )
                }
            }
        }
    }
}

/**
 * 分类项（可折叠）
 */
@Composable
private fun CategoryItem(
    category: SystemCategory,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onChildClick: (SystemChild) -> Unit,
    modifier: Modifier = Modifier
) {
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        label = "arrow_rotation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "收起" else "展开",
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(arrowRotation),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ChildFlowRow(
                    children = category.children,
                    onChildClick = onChildClick
                )
            }
        }
    }
}

/**
 * 子分类流式布局
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChildFlowRow(
    children: List<SystemChild>,
    onChildClick: (SystemChild) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        children.forEach { child ->
            ChildItem(
                child = child,
                onClick = { onChildClick(child) }
            )
        }
    }
}

/**
 * 子分类项
 */
@Composable
private fun ChildItem(
    child: SystemChild,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick
    ) {
        Text(
            text = child.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 12.dp)
        )
    }
}
