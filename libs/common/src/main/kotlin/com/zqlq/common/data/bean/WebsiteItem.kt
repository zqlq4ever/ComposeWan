package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 常用网站项
 * @property icon 图标
 * @property id ID
 * @property link 链接
 * @property name 名称
 * @property order 顺序
 * @property visible 可见性
 *
 * JSON示例：
 * {
 *     "icon": "",
 *     "id": 1,
 *     "link": "https://www.wanandroid.com",
 *     "name": "玩Android",
 *     "order": 0,
 *     "visible": 1
 * }
 */
@Serializable
data class WebsiteItem(
    val icon: String? = null,
    val id: Int = 0,
    val link: String? = null,
    val name: String? = null,
    val order: Int = 0,
    val visible: Int = 0
)
