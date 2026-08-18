package com.zqlq.compose.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/**
 * 下拉选择器：锚点留在原布局，菜单挂到 [android.R.id.content]。
 *
 * @param selectedIndex 当前选中索引，-1 表示未选中。
 * @param placeholder 未选中时的占位文案。
 */
@Composable
fun DropdownSelector(
    items: List<String>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    fillMaxWidth: Boolean = true,
    label: String = "",
    labelWidth: Dp = 75.dp,
    maxMenuHeight: Dp = 220.dp,
    popBackgroundColor: Color = MaterialTheme.colorScheme.surface,
    itemTextSelectColor: Color = MaterialTheme.colorScheme.primary,
    itemTextUnselectColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    anchorBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    anchorBorderColor: Color = Color.Unspecified,
    anchorBorderWidth: Dp = 0.5.dp,
    popBorderColor: Color = MaterialTheme.colorScheme.outlineVariant,
    popBorderWidth: Dp = 0.5.dp,
    showLabel: Boolean = true,
    menuAnchorOffset: Dp = 10.dp,
) {
    var expanded by remember { mutableStateOf(false) }
    var anchorWidthDp by remember { mutableStateOf(0.dp) }
    var anchorBoundsInWindow by remember { mutableStateOf(Rect.Zero) }

    val density = LocalDensity.current
    val hostView = LocalView.current

    val selectedText =
        remember(selectedIndex, items) {
            if (selectedIndex in items.indices) {
                items[selectedIndex]
            } else {
                placeholder
            }
        }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(200),
        label = "dropdown_arrow",
    )

    val containerModifier = if (fillMaxWidth) modifier.fillMaxWidth() else modifier

    Row(modifier = containerModifier, verticalAlignment = Alignment.CenterVertically) {
        if (showLabel) {
            SelectorLabel(
                label = label,
                labelWidth = labelWidth,
            )
        }

        Box(modifier = if (fillMaxWidth) Modifier.weight(1f) else Modifier) {
            SelectorAnchor(
                selectedText = selectedText,
                isSelected = selectedIndex in items.indices,
                rotation = rotation,
                expandToRowRemainder = fillMaxWidth,
                anchorBackgroundColor = anchorBackgroundColor,
                anchorBorderColor = anchorBorderColor,
                anchorBorderWidth = anchorBorderWidth,
                modifier =
                    (if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier)
                        .onGloballyPositioned { coordinates ->
                            anchorBoundsInWindow = coordinates.boundsInWindow()
                        },
                onSizeChanged = { widthPx ->
                    anchorWidthDp = with(density) { widthPx.toDp() }
                },
                onClick = {
                    if (items.isNotEmpty()) {
                        expanded = !expanded
                    }
                },
            )
        }
    }

    if (expanded && items.isNotEmpty()) {
        BackHandler { expanded = false }

        ContentOverlay(visible = true) {
            val contentRoot = remember(hostView) { findContentRoot(hostView) }
            val rootLoc =
                remember(contentRoot) {
                    val loc = IntArray(2)
                    contentRoot?.getLocationInWindow(loc)
                    loc
                }
            val offsetPx = with(density) { menuAnchorOffset.roundToPx() }
            val maxMenuHeightPx = with(density) { maxMenuHeight.roundToPx() }
            val estimatedItemHeightPx = with(density) { 40.dp.roundToPx() }
            val estimatedPanelPaddingPx = with(density) { 12.dp.roundToPx() }
            val estimatedMenuHeightPx =
                minOf(
                    items.size * estimatedItemHeightPx + estimatedPanelPaddingPx,
                    maxMenuHeightPx,
                )
            val menuWidth = anchorWidthDp

            val localLeft = (anchorBoundsInWindow.left - rootLoc[0]).roundToInt()
            val localTop = (anchorBoundsInWindow.top - rootLoc[1]).roundToInt()
            val localBottom = (anchorBoundsInWindow.bottom - rootLoc[1]).roundToInt()
            val rootHeight = contentRoot?.height ?: 0

            var measuredMenuHeightPx by remember { mutableIntStateOf(0) }
            val menuHeightPx = if (measuredMenuHeightPx > 0) measuredMenuHeightPx else estimatedMenuHeightPx

            val spaceBelow = rootHeight - localBottom - offsetPx
            val spaceAbove = localTop - offsetPx
            val placeAbove = spaceBelow < menuHeightPx && spaceAbove > spaceBelow

            val rawMenuY =
                if (placeAbove) {
                    localTop - menuHeightPx - offsetPx
                } else {
                    localBottom + offsetPx
                }
            val menuY =
                if (rootHeight > 0 && menuHeightPx > 0) {
                    rawMenuY.coerceIn(0, (rootHeight - menuHeightPx).coerceAtLeast(0))
                } else {
                    rawMenuY
                }

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { expanded = false },
                            ),
                )

                DropdownMenuPanel(
                    items = items,
                    selectedIndex = selectedIndex,
                    menuWidth = menuWidth,
                    maxMenuHeight = maxMenuHeight,
                    popBackgroundColor = popBackgroundColor,
                    popBorderColor = popBorderColor,
                    popBorderWidth = popBorderWidth,
                    itemTextSelectColor = itemTextSelectColor,
                    itemTextUnselectColor = itemTextUnselectColor,
                    modifier =
                        Modifier
                            .offset { IntOffset(localLeft, menuY) }
                            .width(menuWidth)
                            .onSizeChanged { size ->
                                if (size.height > 0 && size.height != measuredMenuHeightPx) {
                                    measuredMenuHeightPx = size.height
                                }
                            },
                    onItemSelected = { index ->
                        onItemSelected(index)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun DropdownMenuPanel(
    items: List<String>,
    selectedIndex: Int,
    menuWidth: Dp,
    maxMenuHeight: Dp,
    popBackgroundColor: Color,
    popBorderColor: Color,
    popBorderWidth: Dp,
    itemTextSelectColor: Color,
    itemTextUnselectColor: Color,
    modifier: Modifier,
    onItemSelected: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()
    var itemHeightPx by remember { mutableIntStateOf(0) }
    val shape = RoundedCornerShape(10.dp)

    LaunchedEffect(itemHeightPx, selectedIndex) {
        if (selectedIndex >= 0 && itemHeightPx > 0) {
            scrollState.scrollTo(selectedIndex * itemHeightPx)
        }
    }

    val borderMod =
        if (popBorderColor != Color.Unspecified) {
            Modifier.border(popBorderWidth, popBorderColor, shape)
        } else {
            Modifier
        }

    Column(
        modifier =
            modifier
                .clip(shape)
                .then(borderMod)
                .background(popBackgroundColor, shape)
                .padding(horizontal = 6.dp, vertical = 6.dp)
                .heightIn(max = maxMenuHeight)
                .width(menuWidth)
                .verticalScroll(scrollState),
    ) {
        items.forEachIndexed { index, item ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .clickable { onItemSelected(index) }
                        .onSizeChanged { size ->
                            if (index == 0) {
                                itemHeightPx = size.height
                            }
                        },
            ) {
                DropdownItemContent(
                    text = item,
                    isSelected = index == selectedIndex,
                    itemTextSelectColor = itemTextSelectColor,
                    itemTextUnselectColor = itemTextUnselectColor,
                )
            }
        }
    }
}

@Composable
private fun SelectorLabel(
    label: String,
    labelWidth: Dp,
) {
    Text(
        text = label,
        color = MaterialTheme.colorScheme.onSurface,
        fontSize = 14.sp,
        modifier = Modifier.width(labelWidth),
    )
}

@Composable
private fun SelectorAnchor(
    selectedText: String,
    isSelected: Boolean,
    rotation: Float,
    expandToRowRemainder: Boolean,
    anchorBackgroundColor: Color,
    anchorBorderColor: Color,
    anchorBorderWidth: Dp,
    modifier: Modifier,
    onSizeChanged: (Int) -> Unit,
    onClick: () -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant
    val selectedColor = MaterialTheme.colorScheme.onSurface

    val baseModifier =
        modifier
            .onSizeChanged { onSizeChanged(it.width) }
            .clip(shape)
            .background(anchorBackgroundColor)

    val borderModifier =
        if (anchorBorderColor != Color.Unspecified) {
            baseModifier.border(
                width = anchorBorderWidth,
                color = anchorBorderColor,
                shape = shape,
            )
        } else {
            baseModifier
        }

    Box(
        modifier =
            borderModifier
                .clickable { onClick() }
                .padding(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = selectedText,
                color = if (isSelected) selectedColor else unselectedColor,
                fontSize = 14.sp,
                modifier = if (expandToRowRemainder) Modifier.weight(1f) else Modifier,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                tint = selectedColor,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}

@Composable
private fun DropdownItemContent(
    text: String,
    isSelected: Boolean,
    itemTextSelectColor: Color,
    itemTextUnselectColor: Color,
) {
    val shape = RoundedCornerShape(8.dp)
    val itemModifier =
        if (isSelected) {
            Modifier
                .fillMaxWidth()
                .clip(shape)
                .background(itemTextSelectColor.copy(alpha = 0.14f))
                .padding(horizontal = 10.dp, vertical = 10.dp)
        } else {
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        }
    Text(
        text = text,
        color = if (isSelected) itemTextSelectColor else itemTextUnselectColor,
        fontSize = 14.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
        modifier = itemModifier,
    )
}
