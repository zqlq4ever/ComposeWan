package com.zqlq.common.data.request

import kotlinx.serialization.Serializable

/**
 * 搜索请求
 * @property keyword 关键词
 * @property page 页码（从0开始）
 *
 * JSON示例：
 * {
 *     "keyword": "Android",
 *     "page": 0
 * }
 */
@Serializable
data class SearchRequest(
    val keyword: String,
    val page: Int = 0
)
