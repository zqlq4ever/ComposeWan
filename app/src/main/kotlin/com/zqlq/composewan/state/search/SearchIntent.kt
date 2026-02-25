package com.zqlq.composewan.state.search

/**
 * 搜索用户意图
 */
sealed interface SearchIntent {
    /** 更新搜索关键词 */
    data class UpdateQuery(val query: String) : SearchIntent
    
    /** 执行搜索 */
    data object Search : SearchIntent
    
    /** 清除搜索内容 */
    data object Clear : SearchIntent
    
    /** 点击搜索结果 */
    data class ArticleClick(val url: String) : SearchIntent
}

/**
 * 搜索副作用（一次性事件）
 */
sealed interface SearchEffect {
    /** 跳转 WebView */
    data class NavigateToWebView(val url: String) : SearchEffect
}
