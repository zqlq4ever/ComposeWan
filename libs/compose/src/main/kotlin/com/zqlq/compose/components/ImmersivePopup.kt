package com.zqlq.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.zqlq.compose.utils.rememberKeyboardHeight

/**
 * 全屏浮层：挂到 [android.R.id.content]，不新建 Dialog Window。
 *
 * @param applyKeyboardPadding 整页底部避让键盘；内容自行处理时传 false。
 */
@Composable
fun ImmersivePopup(
    onDismissRequest: () -> Unit,
    scrimColor: Color = MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f),
    dismissOnTouchOutside: Boolean = false,
    applyKeyboardPadding: Boolean = true,
    content: @Composable () -> Unit,
) {
    ContentOverlay(visible = true) {
        if (applyKeyboardPadding) {
            ImmersivePopupBodyWithKeyboardPadding(
                onDismissRequest = onDismissRequest,
                scrimColor = scrimColor,
                dismissOnTouchOutside = dismissOnTouchOutside,
                content = content,
            )
        } else {
            ImmersivePopupBodyPlain(
                onDismissRequest = onDismissRequest,
                scrimColor = scrimColor,
                dismissOnTouchOutside = dismissOnTouchOutside,
                content = content,
            )
        }
    }
}

@Composable
private fun ImmersivePopupBodyWithKeyboardPadding(
    onDismissRequest: () -> Unit,
    scrimColor: Color,
    dismissOnTouchOutside: Boolean,
    content: @Composable () -> Unit,
) {
    val keyboardHeight = rememberKeyboardHeight()
    ImmersivePopupScrim(
        onDismissRequest = onDismissRequest,
        scrimColor = scrimColor,
        dismissOnTouchOutside = dismissOnTouchOutside,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(bottom = keyboardHeight),
        ) {
            content()
        }
    }
}

@Composable
private fun ImmersivePopupBodyPlain(
    onDismissRequest: () -> Unit,
    scrimColor: Color,
    dismissOnTouchOutside: Boolean,
    content: @Composable () -> Unit,
) {
    ImmersivePopupScrim(
        onDismissRequest = onDismissRequest,
        scrimColor = scrimColor,
        dismissOnTouchOutside = dismissOnTouchOutside,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
private fun ImmersivePopupScrim(
    onDismissRequest: () -> Unit,
    scrimColor: Color,
    dismissOnTouchOutside: Boolean,
    content: @Composable () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(scrimColor)
                .then(
                    if (dismissOnTouchOutside) {
                        Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onDismissRequest,
                        )
                    } else {
                        Modifier.pointerInput(Unit) {
                            detectTapGestures { }
                        }
                    },
                ),
    ) {
        content()
    }
}
