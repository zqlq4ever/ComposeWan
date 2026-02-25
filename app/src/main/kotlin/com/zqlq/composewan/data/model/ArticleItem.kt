package com.zqlq.composewan.data.model

/**
 * 文章数据类
 * @param id 标识
 * @param title 标题
 * @param author 作者
 * @param time 时间
 * @param url 链接
 * @param chapterName 分类名称
 * @param isCollect 是否收藏
 */
data class ArticleItem(
    val id: Int,
    val title: String,
    val author: String,
    val time: String,
    val url: String,
    val chapterName: String = "",
    val isCollect: Boolean = false
)
