package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 搜索热词项
 * @property id ID
 * @property link 链接
 * @property name 热词名称
 * @property order 顺序
 * @property visible 可见性
 *
 * JSON示例：
 * {
 *     "id": 6,
 *     "link": "",
 *     "name": "Android",
 *     "order": 1,
 *     "visible": 1
 * }
 */
@Serializable
data class HotKeyItem(
    val id: Int = 0,
    val link: String? = null,
    val name: String? = null,
    val order: Int = 0,
    val visible: Int = 0
)
