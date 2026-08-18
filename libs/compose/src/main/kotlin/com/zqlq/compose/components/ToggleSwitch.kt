package com.zqlq.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 胶囊轨道滑动开关。
 *
 * @param isOn 当前是否开启。
 * @param onCheckedChange 切换后的目标值。
 */
@Composable
fun ToggleSwitch(
    isOn: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedTrackColor: Color = MaterialTheme.colorScheme.primary,
    uncheckedTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    thumbColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    val trackWidth = 48.dp
    val trackHeight = 26.dp
    val thumbSize = 22.dp
    val thumbPadding = 2.dp

    val trackShape = RoundedCornerShape(50)

    val trackColor by animateColorAsState(
        targetValue = if (isOn) checkedTrackColor else uncheckedTrackColor,
        animationSpec = tween(durationMillis = 200),
        label = "trackColor",
    )

    val thumbOffsetX by animateDpAsState(
        targetValue = if (isOn) trackWidth - thumbSize - thumbPadding else thumbPadding,
        animationSpec = tween(durationMillis = 200),
        label = "thumbOffset",
    )

    Box(
        modifier =
            modifier
                .size(width = trackWidth, height = trackHeight)
                .clip(trackShape)
                .background(trackColor)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { onCheckedChange(!isOn) },
                ).padding(thumbPadding),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .offset(x = thumbOffsetX)
                    .size(thumbSize)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(thumbColor, CircleShape),
        )
    }
}
