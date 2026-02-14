package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 用户信息
 * @property admin 是否管理员
 * @property chapterTops 收藏的章节ID列表
 * @property coinCount 积分
 * @property collectIds 收藏的文章ID列表
 * @property email 邮箱
 * @property icon 头像
 * @property id 用户ID
 * @property nickname 昵称
 * @property password 密码
 * @property publicName 公开名称
 * @property token 令牌
 * @property type 类型
 * @property username 用户名
 *
 * JSON示例：
 * {
 *     "admin": false,
 *     "chapterTops": [],
 *     "coinCount": 430,
 *     "collectIds": [1567, 1534, 1551],
 *     "email": "",
 *     "icon": "",
 *     "id": 12345,
 *     "nickname": "测试用户",
 *     "password": "",
 *     "publicName": "测试用户",
 *     "token": "",
 *     "type": 0,
 *     "username": "test"
 * }
 */
@Serializable
data class UserInfo(
    val admin: Boolean = false,
    val chapterTops: List<Int>? = null,
    val coinCount: Int = 0,
    val collectIds: List<Int>? = null,
    val email: String? = null,
    val icon: String? = null,
    val id: Int = 0,
    val nickname: String? = null,
    val password: String? = null,
    val publicName: String? = null,
    val token: String? = null,
    val type: Int = 0,
    val username: String? = null
)
