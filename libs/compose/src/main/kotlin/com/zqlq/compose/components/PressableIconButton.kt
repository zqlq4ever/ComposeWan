package com.zqlq.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** 带按压反馈的图标按钮；默认圆形返回。 */
@Composable
fun PressableIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    contentDescription: String? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    iconSize: Dp = 24.dp,
    shape: Shape = CircleShape,
    size: Dp = 40.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = Color.Transparent,
    pressedBackgroundColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier =
            modifier
                .size(size)
                .clip(shape)
                .background(
                    color = if (isPressed) pressedBackgroundColor else backgroundColor,
                ).clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick,
                ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier =
                Modifier
                    .padding(contentPadding)
                    .size(iconSize),
        )
    }
}
