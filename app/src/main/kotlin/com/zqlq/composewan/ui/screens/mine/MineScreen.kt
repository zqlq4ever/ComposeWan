package com.zqlq.composewan.ui.screens.mine

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.zqlq.common.utils.toast.ToastUtils

private val bgUrl1 = "https://picsum.photos/400/300"
private val bgUrl2 = "https://bing.biturl.top/?resolution=1920&format=image&index=random"
private val avatarUrl1 = "https://api.eyabc.cn/api/picture/beauty"
private val avatarUrl2 = "https://api.eyabc.cn/api/picture/mc"

/**
 * 我的屏幕
 * 展示用户信息和菜单列表
 *
 * @param onAboutClick 关于点击回调
 * @param onCollectClick 我的收藏点击回调
 * @param onLoginClick 登录点击回调
 */
@Composable
fun MineScreen(
    onAboutClick: () -> Unit = {},
    onCollectClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showAvatarPreview by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
                HeaderSection(
                    onAvatarClick = { showAvatarPreview = true },
                    onUsernameClick = onLoginClick
                )

                MenuList(
                    onAboutClick = onAboutClick,
                    onCheckUpdateClick = { showUpdateDialog = true },
                    onCollectClick = onCollectClick
                )
            }

            if (showUpdateDialog) {
                UpdateDialog(
                    onDismiss = { showUpdateDialog = false },
                    onUpdate = { showUpdateDialog = false; ToastUtils.show("开始更新...") }
                )
            }

            if (showAvatarPreview) {
                AvatarPreview(
                    imageUrl = bgUrl1,
                    onDismiss = { showAvatarPreview = false }
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
        },
        shape = RoundedCornerShape(8.dp)
    )
}

/**
 * 头部区域
 * 包含背景图、头像、用户名
 *
 * @param onAvatarClick 头像点击回调
 * @param onUsernameClick 用户名点击回调
 */
@Composable
private fun HeaderSection(
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    onUsernameClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(bgUrl2)
                .memoryCachePolicy(CachePolicy.ENABLED)  // 内存缓存
                .diskCachePolicy(CachePolicy.DISABLED)    // 禁用磁盘缓存
                .build(),
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
                model = ImageRequest.Builder(LocalContext.current)
                    .data(avatarUrl2)
                    .memoryCachePolicy(CachePolicy.ENABLED)  // 内存缓存
                    .diskCachePolicy(CachePolicy.DISABLED)    // 禁用磁盘缓存
                    .build(),
                contentDescription = "头像",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onAvatarClick),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "玩Android用户",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.clickable(onClick = onUsernameClick)
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
    onCollectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val menuItems = listOf(
        MenuItem("我的收藏", MenuItemType.COLLECT),
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
                        MenuItemType.COLLECT -> onCollectClick()
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
    SHOW_DIALOG,
    COLLECT
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

/**
 * 头像预览组件
 * 支持缩放、平移、双击缩放
 *
 * @param imageUrl 图片URL
 * @param onDismiss 关闭回调
 */
@Composable
private fun AvatarPreview(
    imageUrl: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        // 缩放和平移状态管理
        var scale by remember { mutableFloatStateOf(1f) }
        var offsetX by remember { mutableFloatStateOf(0f) }
        var offsetY by remember { mutableFloatStateOf(0f) }

        // 变换状态
        val transformState = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(1f, 4f) // 限制缩放范围 1-4倍
            offsetX += offsetChange.x
            offsetY += offsetChange.y
        }

        Box(modifier = Modifier.fillMaxSize()) {
            // 图片显示区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onDoubleTap = {
                                // 双击事件
                                if (scale > 1f) {
                                    // 重置缩放和平移
                                    scale = 1f
                                    offsetX = 0f
                                    offsetY = 0f
                                } else {
                                    // 放大到4倍
                                    scale = 4f
                                }
                            }
                        )
                    }
                    .transformable(state = transformState)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .build(),
                    contentDescription = "头像预览",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
                    contentScale = ContentScale.FillWidth
                )
            }

            // 关闭按钮（放在图片显示区域之上）
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "关闭",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}