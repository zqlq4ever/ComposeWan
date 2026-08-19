package com.zqlq.composewan.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation3.runtime.NavEntryDecorator

/**
 * 过渡期间 NavDisplay 会把非前台 / 正在退出的页面降到 RESUMED 以下。
 * 在此统一吞掉指针事件，避免点穿到退出页（例如返回后误触换肤里的深色选项）。
 *
 * 必须用 [collectAsState] 而不是 [androidx.lifecycle.compose.collectAsStateWithLifecycle]：
 * 后者在 CREATED 时停止收集，会一直停留在过期的 RESUMED，拦截层永远不会出现。
 */
@Composable
fun <T : Any> rememberResumedInteractionNavEntryDecorator(): NavEntryDecorator<T> =
    remember {
        NavEntryDecorator { entry ->
            ResumedInteractionGate {
                entry.Content()
            }
        }
    }

@Composable
private fun ResumedInteractionGate(content: @Composable () -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val interactionsEnabled = lifecycleState.isAtLeast(Lifecycle.State.RESUMED)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .then(
                    if (interactionsEnabled) {
                        Modifier
                    } else {
                        Modifier.pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent(PointerEventPass.Initial)
                                    event.changes.forEach { it.consume() }
                                }
                            }
                        }
                    },
                ),
    ) {
        content()
    }
}
