package com.zqlq.composewan.ui.mine.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import com.zqlq.common.utils.session.UserSession
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.compose.components.ConfirmPop
import com.zqlq.composewan.R

private val bgUrl1 = "https://picsum.photos/400/300"
private val bgUrl2 = "https://bing.biturl.top/?resolution=1920&format=image&index=random"
private val avatarUrl1 = "https://api.eyabc.cn/api/picture/beauty"
private val avatarUrl2 = "https://api.eyabc.cn/api/picture/mc"

/**
 * 我的屏幕
 * 展示用户信息和菜单列表
 *
 * @param modifier 修饰符
 * @param onAboutClick 关于点击回调
 * @param onCollectClick 我的收藏点击回调
 * @param onLoginClick 登录点击回调
 * @param onSettingsClick 设置点击回调
 */
@Composable
fun MineScreen(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit = {},
    onCollectClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
) {
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showAvatarPreview by remember { mutableStateOf(false) }
    val isLoggedIn by UserSession.isLoggedInFlow.collectAsStateWithLifecycle()
    val username by UserSession.usernameFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
                HeaderSection(
                    isLoggedIn = isLoggedIn,
                    displayName = if (isLoggedIn) {
                        username
                    } else {
                        stringResource(R.string.not_logged_in)
                    },
                    onAvatarClick = { showAvatarPreview = true },
                    onUsernameClick = {
                        if (!isLoggedIn) {
                            onLoginClick()
                        }
                    },
                )

                MenuList(
                    onAboutClick = onAboutClick,
                    onCheckUpdateClick = { showUpdateDialog = true },
                    onCollectClick = onCollectClick,
                    onSettingsClick = onSettingsClick,
                )
            }

            if (showUpdateDialog) {
                ConfirmPop(
                    title = stringResource(R.string.update_dialog_title),
                    message = stringResource(R.string.update_dialog_message),
                    confirmText = stringResource(R.string.update_dialog_confirm),
                    cancelText = stringResource(R.string.update_dialog_cancel),
                    onConfirm = {
                        showUpdateDialog = false
                        ToastUtils.show(R.string.update_started)
                    },
                    onCancel = { showUpdateDialog = false },
                    onDismiss = { showUpdateDialog = false },
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
 * 头部区域
 * 包含背景图、头像、用户名
 *
 * @param isLoggedIn 是否已登录
 * @param displayName 头像下方文案
 * @param onAvatarClick 头像点击回调
 * @param onUsernameClick 用户名点击回调，仅未登录时进入登录页
 */
@Composable
private fun HeaderSection(
    isLoggedIn: Boolean,
    displayName: String,
    modifier: Modifier = Modifier,
    onAvatarClick: () -> Unit = {},
    onUsernameClick: () -> Unit = {},
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
            contentDescription = stringResource(R.string.cd_background),
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
                contentDescription = stringResource(R.string.cd_avatar),
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onAvatarClick),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = displayName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.clickable(
                    enabled = !isLoggedIn,
                    onClick = onUsernameClick,
                ),
            )
        }
    }
}

/**
 * 菜单列表：分组圆角卡片 + 色块图标。
 */
@Composable
private fun MenuList(
    onAboutClick: () -> Unit,
    onCheckUpdateClick: () -> Unit,
    onCollectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val collectItem =
        MenuItem(
            title = stringResource(R.string.mine_menu_collect),
            type = MenuItemType.COLLECT,
            icon = Icons.Filled.FavoriteBorder,
            iconTint = colorScheme.primary,
        )
    val generalItems =
        listOf(
            MenuItem(
                title = stringResource(R.string.mine_menu_settings),
                type = MenuItemType.SETTINGS,
                icon = Icons.Filled.Settings,
                iconTint = colorScheme.secondary,
            ),
            MenuItem(
                title = stringResource(R.string.mine_menu_update),
                type = MenuItemType.SHOW_DIALOG,
                icon = Icons.Filled.SystemUpdate,
                iconTint = colorScheme.tertiary,
            ),
            MenuItem(
                title = stringResource(R.string.mine_menu_about),
                type = MenuItemType.NAVIGATE,
                icon = Icons.Filled.Info,
                iconTint = colorScheme.primary,
            ),
        )

    fun onItemClick(type: MenuItemType) {
        when (type) {
            MenuItemType.TOAST -> Unit
            MenuItemType.NAVIGATE -> onAboutClick()
            MenuItemType.SHOW_DIALOG -> onCheckUpdateClick()
            MenuItemType.COLLECT -> onCollectClick()
            MenuItemType.SETTINGS -> onSettingsClick()
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        MineMenuSection(items = listOf(collectItem), onItemClick = ::onItemClick)
        Spacer(modifier = Modifier.height(12.dp))
        MineMenuSection(items = generalItems, onItemClick = ::onItemClick)
    }
}

/**
 * 菜单项类型
 */
enum class MenuItemType {
    TOAST,
    NAVIGATE,
    SHOW_DIALOG,
    COLLECT,
    SETTINGS,
}

/**
 * 菜单项数据
 */
private data class MenuItem(
    val title: String,
    val type: MenuItemType,
    val icon: ImageVector,
    val iconTint: Color,
)

@Composable
private fun MineMenuSection(
    items: List<MenuItem>,
    onItemClick: (MenuItemType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            items.forEachIndexed { index, item ->
                MenuItemRow(
                    item = item,
                    onClick = { onItemClick(item.type) },
                )
                if (index < items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 68.dp, end = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f),
                    )
                }
            }
        }
    }
}

/**
 * 菜单项行：色块图标 + 标题 + 箭头。
 */
@Composable
private fun MenuItemRow(
    item: MenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp)
                .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(item.iconTint.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = item.iconTint,
                modifier = Modifier.size(16.dp),
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = stringResource(R.string.cd_arrow),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
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
                    contentDescription = stringResource(R.string.cd_avatar_preview),
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
                    contentDescription = stringResource(R.string.action_close),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}