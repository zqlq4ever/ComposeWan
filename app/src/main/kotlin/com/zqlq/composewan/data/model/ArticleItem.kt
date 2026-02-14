package com.zqlq.composewan.data.model

/**
 * 文章数据类
 * @param id 标识
 * @param title 标题
 * @param author 作者
 * @param time 时间
 * @param url 链接
 */
data class ArticleItem(
    val id: Int,
    val title: String,
    val author: String,
    val time: String,
    val url: String
)
