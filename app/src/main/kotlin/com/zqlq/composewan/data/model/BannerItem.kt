package com.zqlq.composewan.data.model

/**
 * Banner 数据类
 * @param id 标识
 * @param title 标题
 * @param imageUrl 图片地址
 * @param url 跳转链接
 */
data class BannerItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val url: String
)
