package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 积分信息
 * @property coinCount 积分数量
 * @property level 等级
 * @property rank 排名
 * @property userId 用户ID
 * @property username 用户名
 *
 * JSON示例：
 * {
 *     "coinCount": 430,
 *     "level": 9,
 *     "rank": 123,
 *     "userId": 12345,
 *     "username": "test"
 * }
 */
@Serializable
data class CoinInfo(
    val coinCount: Int = 0,
    val level: Int = 0,
    val rank: String? = null,
    val userId: Int = 0,
    val username: String? = null
)
