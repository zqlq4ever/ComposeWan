package com.zqlq.compose.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 确认弹窗：普通 Compose，不新建 Dialog Window。
 *
 * @param dismissOnBackPress 为 true 时返回键触发 [onCancel]；为 false 时仅拦截返回键。
 * @param onDismiss 预留关闭回调（当前点遮罩不关闭）。
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun ConfirmPop(
    title: String,
    message: String,
    confirmText: String,
    cancelText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit,
    dismissOnBackPress: Boolean = true,
    scrimColor: Color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    messageColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    confirmContainerColor: Color = MaterialTheme.colorScheme.primary,
    confirmContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    cancelContentColor: Color = MaterialTheme.colorScheme.onSurface,
    cancelBorderColor: Color = MaterialTheme.colorScheme.outline,
) {
    BackHandler {
        if (dismissOnBackPress) {
            onCancel()
        }
    }

    ContentOverlay(visible = true) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(scrimColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { },
                    ).padding(horizontal = 32.dp),
            contentAlignment = Alignment.Center,
        ) {
            val cardShape = RoundedCornerShape(16.dp)
            Box(
                modifier =
                    Modifier
                        .widthIn(max = 360.dp)
                        .clip(cardShape)
                        .background(containerColor)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {},
                        ).border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), cardShape),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = title,
                        color = titleColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = message,
                        color = messageColor,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(top = 16.dp, bottom = 32.dp),
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        val buttonShape = RoundedCornerShape(8.dp)

                        Box(
                            modifier =
                                Modifier
                                    .widthIn(min = 80.dp)
                                    .height(42.dp)
                                    .clip(buttonShape)
                                    .border(BorderStroke(1.dp, cancelBorderColor), buttonShape)
                                    .clickable(onClick = onCancel)
                                    .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = cancelText,
                                color = cancelContentColor,
                                fontSize = 15.sp,
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier =
                                Modifier
                                    .widthIn(min = 80.dp)
                                    .height(42.dp)
                                    .clip(buttonShape)
                                    .background(confirmContainerColor)
                                    .clickable(onClick = onConfirm)
                                    .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = confirmText,
                                color = confirmContentColor,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }
        }
    }
}
