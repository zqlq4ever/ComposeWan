package com.zqlq.composewan.data.model

/** 分页文章结果。 */
data class ArticlePage(
    val articles: List<ArticleItem>,
    val over: Boolean,
)
