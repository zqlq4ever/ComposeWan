package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 首页Banner项
 * @property desc 描述
 * @property id ID
 * @property imagePath 图片路径
 * @property isVisible 是否可见
 * @property order 顺序
 * @property title 标题
 * @property type 类型
 * @property url URL
 *
 * JSON示例：
 * {
 *     "desc": "Android高级进阶直播课免费学习",
 *     "id": 29,
 *     "imagePath": "https://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png",
 *     "isVisible": 1,
 *     "order": 0,
 *     "title": "Android高级进阶直播课免费学习",
 *     "type": 0,
 *     "url": "https://url.163.com/4bj"
 * }
 */
@Serializable
data class BannerItem(
    val desc: String? = null,
    val id: Int = 0,
    val imagePath: String? = null,
    val isVisible: Int = 0,
    val order: Int = 0,
    val title: String? = null,
    val type: Int = 0,
    val url: String? = null
)
