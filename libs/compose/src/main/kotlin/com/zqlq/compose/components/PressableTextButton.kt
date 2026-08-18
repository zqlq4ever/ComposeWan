package com.zqlq.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * 带按压遮罩的文字按钮，屏蔽默认水波纹。
 *
 * @param pressedOverlayColor 按压时叠加的遮罩色。
 */
@Composable
fun PressableTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke? = null,
    backgroundColor: Color = Color.Transparent,
    pressedOverlayColor: Color = Color.White.copy(alpha = 0.2f),
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val internalInteractionSource = remember { MutableInteractionSource() }
    val usedInteractionSource = interactionSource ?: internalInteractionSource
    val isPressed by usedInteractionSource.collectIsPressedAsState()

    Box(
        modifier =
            modifier
                .clip(shape)
                .then(
                    if (border != null) {
                        Modifier.border(border, shape)
                    } else {
                        Modifier
                    },
                ).background(backgroundColor),
    ) {
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        color = if (isPressed) pressedOverlayColor else Color.Transparent,
                        shape = shape,
                    ).clickable(
                        interactionSource = usedInteractionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = onClick,
                    ),
        )

        Row(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}
