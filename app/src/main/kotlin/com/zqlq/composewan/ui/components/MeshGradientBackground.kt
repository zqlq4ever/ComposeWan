package com.zqlq.composewan.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import io.github.om252345.composemeshgradient.MeshGradient
import io.github.om252345.composemeshgradient.rememberMeshGradientState
import io.github.om252345.composemeshgradient.utils.SimplexNoise

/**
 * 流体感 Mesh Gradient 背景组件
 *
 * 基于 composemeshgradient 库（OpenGL ES 渲染 + Catmull-Rom 样条插值），
 * 相比官方 MeshGradientPainter 的线性插值，色块过渡更平滑，无网状痕迹。
 *
 * 实现思路（参考库官方 SimplexNoise 示例）：
 * 1. 4×4 网格共 16 个控制点，边界顶点固定在组件边缘，保证流动时四周不留白；
 * 2. 仅内部 4 个顶点用 SimplexNoise 噪声驱动位置偏移，形成自然有机的流动；
 * 3. 目标点与当前点之间做插值平滑，防止噪声跳变导致的画面抖动。
 *
 * 性能设计（避免进入/退出页面时卡顿）：
 * 1. 使用库的 state 版重载（MeshGradientState），update 走零分配快照路径，
 *    不触发整棵组件树每帧重组，帧循环内也不做任何集合分配；
 * 2. 帧循环用 repeatOnLifecycle(RESUMED) 包裹：页面退出转场一开始（ON_PAUSE）
 *    就停止 requestRender，GL 线程先空闲下来。因为 GLSurfaceView 的
 *    onDetachedFromWindow 会阻塞主线程等 GL 线程退出，提前空闲可大幅缩短退出卡顿；
 * 3. globalSubdivisions 从默认 32 降为 16，网格几何量降为 1/4，
 *    首帧 mesh 构建和每帧 GPU 开销同步降低。
 *
 * @param modifier 外层容器修饰符
 * @param animated 是否开启动态流动效果，默认静态
 * @param content 叠加上去的内容
 */
@Composable
fun MeshGradientBackground(
    modifier: Modifier = Modifier,
    animated: Boolean = false,
    content: @Composable () -> Unit,
) {
    // 初始网格点：均匀分布在 0..1 归一化坐标系，边界点即组件边缘
    val initialPoints =
        remember {
            Array(MESH_SIZE * MESH_SIZE) { i ->
                val col = i % MESH_SIZE
                val row = i / MESH_SIZE
                Offset(x = col / (MESH_SIZE - 1f), y = row / (MESH_SIZE - 1f))
            }
        }

    // 网格状态：库内部持有可复用的快照数组，渲染读取零分配
    val meshState =
        rememberMeshGradientState(
            points = initialPoints,
            colors = Palette.toTypedArray(),
        )

    if (animated) {
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(lifecycleOwner) {
            // 仅在页面 RESUMED 时驱动动画，退出转场/切后台立即停止渲染请求
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                var time = 0f
                val basePoints = initialPoints.toList()
                val currentPoints = initialPoints.toMutableList()
                val targetPoints = initialPoints.toMutableList()
                var lastFrameTime = 0L

                while (true) {
                    // 挂起等待下一帧，返回后再在协程体内执行计算与状态更新
                    val frameTime = withFrameNanos { it }

                    // 计算帧间隔，保证动画速度与帧率无关
                    if (lastFrameTime == 0L) lastFrameTime = frameTime
                    val deltaTime = (frameTime - lastFrameTime) / 1_000_000_000f
                    lastFrameTime = frameTime
                    time += deltaTime * FLOW_SPEED

                    // 内部顶点用噪声计算目标位置，边界顶点保持固定
                    for (i in targetPoints.indices) {
                        val col = i % MESH_SIZE
                        val row = i / MESH_SIZE
                        val isBorder =
                            row == 0 || row == MESH_SIZE - 1 ||
                                col == 0 || col == MESH_SIZE - 1
                        if (!isBorder) {
                            val bp = basePoints[i]
                            val noiseX =
                                SimplexNoise.noise(bp.x * 1.5f, time + i) * NOISE_AMPLITUDE
                            val noiseY =
                                SimplexNoise.noise(bp.y * 1.5f, time + i + 100f) *
                                    NOISE_AMPLITUDE
                            targetPoints[i] = Offset(bp.x + noiseX, bp.y + noiseY)
                        }
                    }

                    // 当前点向目标点插值，消除噪声突变带来的抖动
                    for (i in currentPoints.indices) {
                        currentPoints[i] =
                            lerpOffset(currentPoints[i], targetPoints[i], 8f * deltaTime)
                    }

                    // 直接复用列表更新状态，不做快照拷贝
                    meshState.snapAllPoints(currentPoints)
                }
            }
        }
    }

    Box(modifier = modifier) {
        // state 版重载：points/colors 变化走 AndroidView update 零分配路径
        MeshGradient(
            modifier = Modifier.matchParentSize(),
            width = MESH_SIZE,
            height = MESH_SIZE,
            globalSubdivisions = SUBDIVISIONS,
            state = meshState,
        )
        content()
    }
}

/** Offset 线性插值 */
private fun lerpOffset(
    start: Offset,
    stop: Offset,
    fraction: Float,
): Offset {
    val f = fraction.coerceIn(0f, 1f)
    return Offset(
        x = start.x + (stop.x - start.x) * f,
        y = start.y + (stop.y - start.y) * f,
    )
}

private const val MESH_SIZE = 4

/** 网格细分数：每个网格单元再切成 16×16 的小三角形面片，值越小 GPU 越省 */
private const val SUBDIVISIONS = 16

/** 流动速度，值越大流动越快 */
private const val FLOW_SPEED = 0.25f

/** 噪声偏移幅度，控制色块流动范围 */
private const val NOISE_AMPLITUDE = 0.18f

// 低饱和粉紫蓝调，大面积留白，接近系统设置页背景
private val SoftPink = Color(0xFFF8E8F0)
private val SoftLavender = Color(0xFFEFE8F8)
private val SoftBlue = Color(0xFFE8EEF8)
private val SoftCream = Color(0xFFFAF6F2)

// 4×4 网格颜色布局：左上粉 → 中间紫 → 右下蓝，四角用乳白过渡
private val Palette =
    listOf(
        SoftPink, SoftPink, SoftCream, SoftCream,
        SoftPink, SoftLavender, SoftLavender, SoftCream,
        SoftLavender, SoftLavender, SoftBlue, SoftBlue,
        SoftCream, SoftCream, SoftBlue, SoftBlue,
    )
