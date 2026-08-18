package com.zqlq.compose.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.Window
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/** 沉浸式：全屏隐藏系统栏，键盘弹收时维持隐藏。手机底栏场景不要全局调用。 */
object ImmersiveUtil {
    /** API 30 以下：SYSTEM_UI_FLAG 沉浸式，系统栏重现时立刻再藏。 */
    @Suppress("DEPRECATION")
    fun setupImmersiveLegacyWithListener(window: Window) {
        val decorView = window.decorView
        val flags = buildImmersiveFlags()

        decorView.systemUiVisibility = flags

        val listener =
            View.OnSystemUiVisibilityChangeListener { visibility ->
                if ((visibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0 ||
                    (visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0
                ) {
                    decorView.systemUiVisibility = flags
                }
            }
        decorView.setOnSystemUiVisibilityChangeListener(listener)
    }

    /** 键盘收起或浮层退出后重新隐藏系统栏。 */
    fun reapplyImmersive(window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val decorView = window.decorView
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowCompat.getInsetsController(window, decorView)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        } else {
            setupImmersiveLegacyWithListener(window)
        }
    }

    fun reapplyImmersive(context: Context) {
        context.findActivity()?.window?.let { reapplyImmersive(it) }
    }

    @Suppress("DEPRECATION")
    fun buildImmersiveFlags(): Int =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN or
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

    /** 键盘弹收时再藏系统栏。 */
    fun setupKeyboardGuard(window: Window) {
        val decorView = window.decorView
        ViewCompat.setOnApplyWindowInsetsListener(decorView) { v, insets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val ctrl = WindowCompat.getInsetsController(window, v)
                ctrl.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                @Suppress("DEPRECATION")
                val flags = buildImmersiveFlags()
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = flags
                @Suppress("DEPRECATION")
                decorView.post { decorView.systemUiVisibility = flags }
            }
            insets
        }
    }

    fun clearKeyboardGuard(window: Window) {
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView, null)
    }
}

/** 进入 Composition 开启沉浸式，离开后恢复键盘守卫并重新应用。 */
@Composable
fun ImmersiveEffect() {
    val view = LocalView.current
    val localActivity = LocalActivity.current
    val context = LocalContext.current
    val activity = localActivity ?: context.findActivity()

    DisposableEffect(activity) {
        val window = activity?.window
        if (window == null) {
            onDispose { }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, view)
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            ImmersiveUtil.setupKeyboardGuard(window)
            onDispose {
                ImmersiveUtil.clearKeyboardGuard(window)
                ImmersiveUtil.reapplyImmersive(window)
            }
        } else {
            ImmersiveUtil.setupImmersiveLegacyWithListener(window)
            ImmersiveUtil.setupKeyboardGuard(window)
            onDispose {
                ImmersiveUtil.clearKeyboardGuard(window)
                ImmersiveUtil.reapplyImmersive(window)
            }
        }
    }
}
