package com.zqlq.composewan.ui.screens.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zqlq.common.utils.toast.ToastUtils

/**
 * 我的屏幕
 * 展示用户信息和菜单列表
 *
 * @param onAboutClick 关于点击回调
 */
@Composable
fun MineScreen(
    onAboutClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showUpdateDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize()) {
        HeaderSection()

        MenuList(
            onAboutClick = onAboutClick,
            onCheckUpdateClick = { showUpdateDialog = true }
        )

        if (showUpdateDialog) {
            UpdateDialog(
                onDismiss = { showUpdateDialog = false },
                onUpdate = { showUpdateDialog = false; ToastUtils.show("开始更新...") }
            )
        }
    }
}

/**
 * 更新提示对话框
 *
 * @param onDismiss 关闭回调
 * @param onUpdate 更新回调
 */
@Composable
fun UpdateDialog(
    onDismiss: () -> Unit,
    onUpdate: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("发现新版本")
                Icon(
                    imageVector = Icons.Filled.Upgrade,
                    contentDescription = "升级",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.wrapContentHeight()
            ) {
                Text("版本号: 2.0.0")
                Text(
                    text = "更新内容:",
                    fontWeight = FontWeight.Bold
                )
                Text("• 修复了已知bug")
                Text("• 优化了应用性能")
                Text("• 新增了多项功能")
                Text("• 提升了用户体验")
            }
        },
        confirmButton = {
            Button(onClick = onUpdate) {
                Text("立即更新")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("稍后再说")
            }
        }
    )
}

/**
 * 头部区域
 * 包含背景图、头像、用户名
 */
@Composable
private fun HeaderSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {
        AsyncImage(
            model = "https://picsum.photos/800/533",
            contentDescription = "背景图",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.5f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = "https://picsum.photos/200/200",
                contentDescription = "头像",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "玩Android用户",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * 菜单列表
 */
@Composable
private fun MenuList(
    onAboutClick: () -> Unit,
    onCheckUpdateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        MenuItem("我的收藏", MenuItemType.TOAST),
        MenuItem("检查更新", MenuItemType.SHOW_DIALOG),
        MenuItem("关于", MenuItemType.NAVIGATE)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(menuItems) { item ->
            MenuItemRow(
                item = item,
                onClick = {
                    when (item.type) {
                        MenuItemType.TOAST -> ToastUtils.show("点击了: ${item.title}")
                        MenuItemType.NAVIGATE -> onAboutClick()
                        MenuItemType.SHOW_DIALOG -> onCheckUpdateClick()
                    }
                }
            )
        }
    }
}

/**
 * 菜单项类型
 */
enum class MenuItemType {
    TOAST,
    NAVIGATE,
    SHOW_DIALOG
}

/**
 * 菜单项数据
 */
private data class MenuItem(
    val title: String,
    val type: MenuItemType = MenuItemType.TOAST
)

/**
 * 菜单项行
 */
@Composable
private fun MenuItemRow(
    item: MenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "箭头",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
