package com.zqlq.compose.components

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.findViewTreeSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.zqlq.compose.ui.theme.ComposeWanTheme
import com.zqlq.compose.utils.findActivity

/**
 * 将内容挂到 [android.R.id.content]，不新建 Dialog / Popup Window。
 *
 * @param visible 为 true 时挂载全屏层；为 false 时拆卸。
 */
@Composable
fun ContentOverlay(
    visible: Boolean,
    content: @Composable () -> Unit,
) {
    val hostView = LocalView.current
    val holder = remember { OverlayContentHolder() }
    holder.content = content

    DisposableEffect(visible, hostView) {
        if (!visible) {
            return@DisposableEffect onDispose { }
        }
        val root = findContentRoot(hostView) ?: return@DisposableEffect onDispose { }
        val context = hostView.context
        val composeView =
            ComposeView(context).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                hostView.findViewTreeLifecycleOwner()?.let { setViewTreeLifecycleOwner(it) }
                hostView.findViewTreeViewModelStoreOwner()?.let { setViewTreeViewModelStoreOwner(it) }
                hostView.findViewTreeSavedStateRegistryOwner()?.let { setViewTreeSavedStateRegistryOwner(it) }
                setContent {
                    ComposeWanTheme {
                        holder.content?.invoke()
                    }
                }
            }
        root.addView(
            composeView,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            ),
        )
        onDispose {
            composeView.disposeComposition()
            composeView.clearFocus()
            (context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)
                ?.hideSoftInputFromWindow(composeView.windowToken, 0)
            (composeView.parent as? ViewGroup)?.removeView(composeView)
        }
    }
}

/** 可更新内容槽，父组合刷新时驱动浮层重组。 */
private class OverlayContentHolder {
    var content by mutableStateOf<(@Composable () -> Unit)?>(null)
}

/** 优先 Activity 的 [android.R.id.content]，否则沿 View 树向上查找可添加子 View 的容器。 */
internal fun findContentRoot(view: View): ViewGroup? {
    val activity = view.context.findActivity()
    activity?.findViewById<ViewGroup>(android.R.id.content)?.let { return it }

    var current: View? = view.rootView
    if (current is ViewGroup) return current
    current = view.parent as? View
    while (current != null) {
        if (current is ViewGroup && current.id == android.R.id.content) return current
        current = current.parent as? View
    }
    return view.rootView as? ViewGroup
}
