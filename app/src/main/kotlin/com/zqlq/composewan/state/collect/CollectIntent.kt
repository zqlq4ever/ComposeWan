package com.zqlq.composewan.state.collect

import com.zqlq.composewan.data.model.ArticleItem

/**
 * 收藏页面意图
 */
sealed interface CollectIntent {
    /** 加载收藏列表 */
    data object LoadCollectList : CollectIntent
    
    /** 刷新收藏列表 */
    data object RefreshCollectList : CollectIntent
    
    /** 加载更多收藏列表 */
    data object LoadMoreCollectList : CollectIntent
    
    /** 点击文章 */
    data class OnArticleClick(val article: ArticleItem) : CollectIntent
    
    /** 取消收藏 */
    data class OnUncollectClick(val article: ArticleItem) : CollectIntent
}
