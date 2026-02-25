package com.zqlq.composewan.state.system

import com.zqlq.composewan.data.model.SystemCategory

/**
 * 体系页面 UI 状态
 *
 * @param categories 分类列表
 * @param expandedIds 展开的分类ID集合
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
data class SystemState(
    val categories: List<SystemCategory> = emptyList(),
    val expandedIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 体系页面用户意图
 */
sealed interface SystemIntent {
    /** 加载数据 */
    data object LoadData : SystemIntent
    
    /** 切换分类展开状态 */
    data class ToggleExpand(val categoryId: Int) : SystemIntent
    
    /** 点击子分类 */
    data class ChildClick(val categoryName: String, val children: List<com.zqlq.composewan.data.model.SystemChild>) : SystemIntent
}

/**
 * 体系页面副作用
 */
sealed interface SystemEffect {
    /** 显示 Toast */
    data class ShowToast(val message: String) : SystemEffect
    
    /** 跳转到体系详情页面 */
    data class NavigateToSystemDetail(val categoryName: String, val children: List<com.zqlq.composewan.data.model.SystemChild>) : SystemEffect
}
