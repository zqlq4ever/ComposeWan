package com.zqlq.composewan.state.collect

/**
 * 收藏页面副作用
 */
sealed interface CollectEffect {
    /** 导航到WebView */
    data class NavigateToWebView(val url: String) : CollectEffect
    
    /** 显示消息 */
    data class ShowMessage(val message: String) : CollectEffect
}
