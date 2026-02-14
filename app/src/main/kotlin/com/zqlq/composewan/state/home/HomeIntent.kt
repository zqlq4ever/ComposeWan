package com.zqlq.composewan.state.home

/**
 * 首页用户意图
 */
sealed interface HomeIntent {
    /** 加载数据 */
    data object LoadData : HomeIntent
    
    /** 刷新数据 */
    data object Refresh : HomeIntent
    
    /** 加载更多文章 */
    data object LoadMore : HomeIntent
    
    /** 点击 Banner */
    data class BannerClick(val url: String) : HomeIntent
    
    /** 点击文章 */
    data class ArticleClick(val url: String) : HomeIntent
    
    /** 点击搜索 */
    data object SearchClick : HomeIntent
}

/**
 * 首页副作用（一次性事件）
 */
sealed interface HomeEffect {
    /** 跳转 WebView */
    data class NavigateToWebView(val url: String) : HomeEffect
    
    /** 跳转搜索页 */
    data object NavigateToSearch : HomeEffect
}
