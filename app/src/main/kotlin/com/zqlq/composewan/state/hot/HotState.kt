package com.zqlq.composewan.state.hot

import com.zqlq.composewan.data.model.HotKeyItem
import com.zqlq.composewan.data.model.WebsiteItem

/**
 * 热门页面 UI 状态
 *
 * @param hotKeys 热词列表
 * @param websites 常用网站列表
 * @param isLoading 是否加载中
 * @param error 错误信息
 */
data class HotState(
    val hotKeys: List<HotKeyItem> = emptyList(),
    val websites: List<WebsiteItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * 热门页面用户意图
 */
sealed interface HotIntent {
    /** 加载数据 */
    data object LoadData : HotIntent
    
    /** 点击热词 */
    data class HotKeyClick(val name: String) : HotIntent
    
    /** 点击网站 */
    data class WebsiteClick(val url: String) : HotIntent
}

/**
 * 热门页面副作用
 */
sealed interface HotEffect {
    /** 显示 Toast */
    data class ShowToast(val message: String) : HotEffect
    
    /** 跳转 WebView */
    data class NavigateToWebView(val url: String) : HotEffect
    
    /** 跳转到搜索页面 */
    data class NavigateToSearch(val keyword: String) : HotEffect
}
