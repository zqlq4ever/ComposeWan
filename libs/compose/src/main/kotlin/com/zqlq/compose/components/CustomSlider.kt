package com.zqlq.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/** 滑块颜色。 */
data class CustomSliderColors(
    val thumbColor: Color,
    val activeTrackColor: Color,
    val inactiveTrackColor: Color,
    val disabledThumbColor: Color,
    val disabledActiveTrackColor: Color,
    val disabledInactiveTrackColor: Color,
)

/** 滑块尺寸。 */
data class CustomSliderDimensions(
    val trackHeight: Dp = 4.dp,
    val thumbRadius: Dp = 8.dp,
    val trackCornerRadius: Dp = 2.dp,
)

object CustomSliderDefaults {
    @Composable
    fun colors(
        thumbColor: Color = MaterialTheme.colorScheme.onPrimary,
        activeTrackColor: Color = MaterialTheme.colorScheme.primary,
        inactiveTrackColor: Color = MaterialTheme.colorScheme.surfaceVariant,
        disabledThumbColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        disabledActiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledInactiveTrackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    ): CustomSliderColors =
        CustomSliderColors(
            thumbColor = thumbColor,
            activeTrackColor = activeTrackColor,
            inactiveTrackColor = inactiveTrackColor,
            disabledThumbColor = disabledThumbColor,
            disabledActiveTrackColor = disabledActiveTrackColor,
            disabledInactiveTrackColor = disabledInactiveTrackColor,
        )
}

/**
 * 自定义滑块，可选标签与数值。
 *
 * @param dragExitThresholdDp 手指纵向滑出该阈值后结束拖拽。
 */
@Composable
fun CustomSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: IntRange = 0..100,
    steps: Int = 0,
    onValueChangeFinished: (() -> Unit)? = null,
    colors: CustomSliderColors = CustomSliderDefaults.colors(),
    dimensions: CustomSliderDimensions = CustomSliderDimensions(),
    thumbShape: Shape = CircleShape,
    showValueLabel: Boolean = false,
    valueLabel: @Composable ((value: Float) -> Unit)? = null,
    label: String = "",
    showValue: Boolean = true,
    valueFormat: (Int) -> String = { it.toString() },
    dragExitThresholdDp: Dp = 50.dp,
) {
    val labelColor =
        if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    if (label.isNotEmpty() || showValue) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    color = labelColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(70.dp),
                )
            }

            SliderContent(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = onValueChangeFinished,
                colors = colors,
                dimensions = dimensions,
                thumbShape = thumbShape,
                showValueLabel = showValueLabel,
                valueLabel = valueLabel,
                dragExitThresholdDp = dragExitThresholdDp,
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth(),
            )

            if (showValue) {
                Text(
                    text = valueFormat(value),
                    color = labelColor,
                    fontSize = 14.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(44.dp),
                    textAlign = TextAlign.End,
                )
            }
        }
    } else {
        SliderContent(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
            onValueChangeFinished = onValueChangeFinished,
            colors = colors,
            dimensions = dimensions,
            thumbShape = thumbShape,
            showValueLabel = showValueLabel,
            valueLabel = valueLabel,
            dragExitThresholdDp = dragExitThresholdDp,
            modifier = modifier,
        )
    }
}

@Composable
private fun SliderContent(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier,
    enabled: Boolean,
    valueRange: IntRange,
    steps: Int,
    onValueChangeFinished: (() -> Unit)?,
    colors: CustomSliderColors,
    dimensions: CustomSliderDimensions,
    thumbShape: Shape,
    showValueLabel: Boolean,
    valueLabel: @Composable ((value: Float) -> Unit)?,
    dragExitThresholdDp: Dp,
) {
    var sliderSize by remember { mutableStateOf(IntSize.Zero) }
    var isDragging by remember { mutableStateOf(false) }
    val localDensity = LocalDensity.current
    val dragExitThresholdPx = with(localDensity) { dragExitThresholdDp.toPx() }

    val animatedThumbScale by animateFloatAsState(
        targetValue = if (isDragging) 1.2f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "thumb_scale",
    )

    val rangeSpan = (valueRange.last - valueRange.first).toFloat().coerceAtLeast(1f)
    val normalizedValue = (value - valueRange.first).toFloat() / rangeSpan
    val activeTrackColor = if (enabled) colors.activeTrackColor else colors.disabledActiveTrackColor
    val inactiveTrackColor = if (enabled) colors.inactiveTrackColor else colors.disabledInactiveTrackColor
    val thumbColor = if (enabled) colors.thumbColor else colors.disabledThumbColor

    fun calculateValue(offset: Float): Float {
        val sliderWidth = sliderSize.width
        if (sliderWidth == 0) return value.toFloat()

        var newValue = (offset / sliderWidth) * (valueRange.last - valueRange.first) + valueRange.first
        if (steps > 0) {
            val stepSize = (valueRange.last - valueRange.first) / steps
            newValue = (valueRange.first + (newValue / stepSize).toInt() * stepSize).toFloat()
        }

        return newValue.coerceIn(valueRange.first.toFloat(), valueRange.last.toFloat())
    }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(dimensions.thumbRadius * 2 + 8.dp)
                .onSizeChanged { sliderSize = it }
                .pointerInput(enabled) {
                    if (!enabled) return@pointerInput

                    detectTapGestures { offset ->
                        val newValue = calculateValue(offset.x)
                        onValueChange(newValue.toInt())
                        onValueChangeFinished?.invoke()
                    }
                }
                .pointerInput(enabled, dragExitThresholdPx) {
                    if (!enabled) return@pointerInput

                    detectHorizontalDragGestures(
                        dragExitThresholdPx = dragExitThresholdPx,
                        onDragStart = { offset ->
                            isDragging = true
                            val newValue = calculateValue(offset.x)
                            onValueChange(newValue.toInt())
                        },
                        onDragEnd = {
                            isDragging = false
                            onValueChangeFinished?.invoke()
                        },
                        onDragCancel = {
                            isDragging = false
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            val newValue = calculateValue(change.position.x)
                            onValueChange(newValue.toInt())
                        },
                    )
                },
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(dimensions.trackHeight)
                    .clip(RoundedCornerShape(dimensions.trackCornerRadius))
                    .background(inactiveTrackColor),
        )

        Box(
            modifier =
                Modifier
                    .fillMaxWidth(normalizedValue.coerceIn(0f, 1f))
                    .height(dimensions.trackHeight)
                    .clip(RoundedCornerShape(dimensions.trackCornerRadius))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(activeTrackColor, activeTrackColor.copy(alpha = 0.8f)),
                        ),
                    ),
        )

        val thumbOffsetPx =
            normalizedValue * sliderSize.width - with(LocalDensity.current) { dimensions.thumbRadius.toPx() }

        Box(
            modifier =
                Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            placeable.placeRelative(
                                x = thumbOffsetPx.coerceIn(0f, sliderSize.width - placeable.width.toFloat()).toInt(),
                                y = 0,
                            )
                        }
                    }
                    .size(dimensions.thumbRadius * 2 * animatedThumbScale)
                    .shadow(4.dp, thumbShape)
                    .background(thumbColor, thumbShape),
        )

        if (showValueLabel && valueLabel != null) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(bottom = dimensions.thumbRadius * 2 + 4.dp),
            ) {
                valueLabel(value.toFloat())
            }
        }
    }
}

/** 水平优先拖拽；垂直滚动不拦截；纵向滑出阈值后结束。 */
private suspend fun PointerInputScope.detectHorizontalDragGestures(
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit,
    dragExitThresholdPx: Float,
) {
    awaitEachGesture {
        val down = awaitFirstDown(requireUnconsumed = false)
        var dragAccepted = false

        while (!dragAccepted) {
            val event = awaitPointerEvent()
            val change = event.changes.firstOrNull { it.id == down.id } ?: continue

            if (change.isConsumed) {
                onDragCancel()
                return@awaitEachGesture
            }
            if (!change.pressed) {
                return@awaitEachGesture
            }

            val cumulativeOffset = change.position - down.position
            val touchSlop = viewConfiguration.touchSlop

            if (abs(cumulativeOffset.x) > touchSlop && abs(cumulativeOffset.x) >= abs(cumulativeOffset.y)) {
                dragAccepted = true
                change.consume()
                onDragStart(change.position)
                onDrag(change, Offset(cumulativeOffset.x, 0f))
            }

            if (abs(cumulativeOffset.y) > touchSlop && abs(cumulativeOffset.y) > abs(cumulativeOffset.x)) {
                onDragCancel()
                return@awaitEachGesture
            }
        }

        while (true) {
            val event = awaitPointerEvent(PointerEventPass.Main)
            val currentChange = event.changes.firstOrNull { it.id == down.id } ?: continue

            if (!currentChange.pressed) {
                onDragEnd()
                break
            }
            if (currentChange.isConsumed) {
                onDragCancel()
                break
            }

            val yOffset = abs(currentChange.position.y - down.position.y)
            if (yOffset > dragExitThresholdPx) {
                onDragEnd()
                break
            }

            currentChange.consume()
            val dragDeltaX = (currentChange.position - currentChange.previousPosition).x
            onDrag(currentChange, Offset(dragDeltaX, 0f))
        }
    }
}
