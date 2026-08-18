package com.zqlq.compose.utils

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/** 屏高 10%（≥80px）以下视为键盘未弹出。 */
internal fun minKeyboardHeightPx(screenHeightPx: Int): Int = (screenHeightPx * 0.1f).toInt().coerceAtLeast(80)

/** 弹出约 80ms 稳定后再抬高；收起立刻归零，避免垫高闪烁。 */
@Composable
internal fun rememberSnappedKeyboardHeight(
    rawHeight: Dp,
    screenHeightPx: Int,
): Dp {
    val density = LocalDensity.current
    val minDp = with(density) { minKeyboardHeightPx(screenHeightPx).toDp() }
    val candidate = if (rawHeight >= minDp) rawHeight else 0.dp
    var snapped by remember { mutableStateOf(0.dp) }

    LaunchedEffect(candidate) {
        when {
            candidate == 0.dp -> {
                snapped = 0.dp
            }

            candidate > snapped -> {
                delay(80.milliseconds)
                snapped = candidate
            }

            else -> {
                snapped = 0.dp
            }
        }
    }
    return snapped
}

/** 用 visibleFrame 测量软键盘高度。 */
@Composable
fun rememberKeyboardHeight(): Dp {
    val density = LocalDensity.current
    var rawHeight by remember { mutableStateOf(0.dp) }
    var screenHeightPx by remember { mutableIntStateOf(1) }

    val view = LocalView.current
    DisposableEffect(view) {
        val listener =
            ViewTreeObserver.OnGlobalLayoutListener {
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val screenHeight = view.rootView.height.coerceAtLeast(1)
                screenHeightPx = screenHeight
                val keypadHeight = (screenHeight - rect.bottom).coerceAtLeast(0)
                rawHeight = with(density) { keypadHeight.toDp() }
            }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return rememberSnappedKeyboardHeight(rawHeight, screenHeightPx)
}

/** API 30+ 以 IME insets 为主，GlobalLayout 兜底。 */
@Composable
fun rememberKeyboardHeightCompat(): Dp {
    val density = LocalDensity.current
    val view = LocalView.current

    val imeHeight = WindowInsets.ime.getBottom(density)
    val imeDp = with(density) { imeHeight.toDp() }

    var manualHeight by remember { mutableStateOf(0.dp) }
    var screenHeightPx by remember { mutableIntStateOf(1) }
    DisposableEffect(view) {
        val listener =
            ViewTreeObserver.OnGlobalLayoutListener {
                val rect = Rect()
                view.getWindowVisibleDisplayFrame(rect)
                val screenHeight = view.rootView.height.coerceAtLeast(1)
                screenHeightPx = screenHeight
                val keypadHeight = (screenHeight - rect.bottom).coerceAtLeast(0)
                manualHeight = with(density) { keypadHeight.toDp() }
            }
        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }

    val raw = if (imeDp > 0.dp) imeDp else manualHeight
    val screenPx =
        screenHeightPx.coerceAtLeast(
            view.rootView.height.coerceAtLeast(1),
        )
    return rememberSnappedKeyboardHeight(raw, screenPx)
}

/** Overlay 测高：API 30+ 走 Compat，否则 PopupWindow。 */
@Composable
fun rememberOverlayKeyboardHeight(): Dp =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        rememberKeyboardHeightCompat()
    } else {
        rememberKeyboardHeightByPopup()
    }

/** API 30 以下用独立 PopupWindow 测键盘高度，不改 Activity softInputMode。 */
@Composable
fun rememberKeyboardHeightByPopup(): Dp {
    var rawHeight by remember { mutableStateOf(0.dp) }
    var screenHeightPx by remember { mutableIntStateOf(1) }
    val density = LocalDensity.current
    val localActivity = LocalActivity.current
    val context = LocalContext.current
    val anchorView = LocalView.current

    DisposableEffect(localActivity, context, anchorView) {
        val activity = localActivity ?: context.findActivity()
        if (activity == null) {
            onDispose { }
        } else {
            val popupContent = FrameLayout(activity)
            val popup =
                PopupWindow(activity).apply {
                    contentView = popupContent
                    width = 0
                    height = ViewGroup.LayoutParams.MATCH_PARENT
                    setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                    isTouchable = false
                    isFocusable = false
                    isOutsideTouchable = false
                    @Suppress("DEPRECATION")
                    softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                    inputMethodMode = PopupWindow.INPUT_METHOD_NEEDED
                }

            var baselineGapPx = -1

            val listener =
                ViewTreeObserver.OnGlobalLayoutListener {
                    val rect = Rect()
                    popupContent.getWindowVisibleDisplayFrame(rect)
                    val screenHeight =
                        activity.resources.displayMetrics.heightPixels
                            .coerceAtLeast(1)
                    screenHeightPx = screenHeight
                    val gapPx = (screenHeight - rect.bottom).coerceAtLeast(0)

                    if (baselineGapPx < 0) {
                        baselineGapPx = gapPx
                    }

                    val keyboardPx = (gapPx - baselineGapPx).coerceAtLeast(0)
                    val minKeyboardPx = minKeyboardHeightPx(screenHeight)
                    rawHeight =
                        if (keyboardPx >= minKeyboardPx) {
                            with(density) { keyboardPx.toDp() }
                        } else {
                            if (gapPx <= baselineGapPx + 8) {
                                baselineGapPx = gapPx
                            }
                            0.dp
                        }
                }
            popupContent.viewTreeObserver.addOnGlobalLayoutListener(listener)

            val showRunnable =
                Runnable {
                    if (!popup.isShowing && anchorView.windowToken != null) {
                        popup.showAtLocation(
                            activity.window.decorView,
                            Gravity.NO_GRAVITY,
                            0,
                            0,
                        )
                    }
                }
            anchorView.post(showRunnable)

            onDispose {
                anchorView.removeCallbacks(showRunnable)
                popupContent.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                if (popup.isShowing) {
                    popup.dismiss()
                }
            }
        }
    }

    return rememberSnappedKeyboardHeight(rawHeight, screenHeightPx)
}

/** 进入 Composition 临时 ADJUST_RESIZE，离开后恢复。输入页慎用。 */
@Composable
fun SoftInputAdjustResizeEffect() {
    val localActivity = LocalActivity.current
    val context = LocalContext.current

    DisposableEffect(localActivity, context) {
        val window = (localActivity ?: context.findActivity())?.window
        if (window == null) {
            onDispose { }
        } else {
            val previous = window.attributes.softInputMode
            @Suppress("DEPRECATION")
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            onDispose { window.setSoftInputMode(previous) }
        }
    }
}

/** 进入 Composition 临时 ADJUST_NOTHING（禁止系统顶窗），离开后恢复。 */
@Composable
fun SoftInputAdjustNothingEffect() {
    val localActivity = LocalActivity.current
    val context = LocalContext.current

    DisposableEffect(localActivity, context) {
        val window = (localActivity ?: context.findActivity())?.window
        if (window == null) {
            onDispose { }
        } else {
            val previous = window.attributes.softInputMode
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
            onDispose { window.setSoftInputMode(previous) }
        }
    }
}
