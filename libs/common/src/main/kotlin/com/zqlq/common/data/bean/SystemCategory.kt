package com.zqlq.common.data.bean

import kotlinx.serialization.Serializable

/**
 * 体系分类项
 * @property children 子分类
 * @property courseId 课程ID
 * @property id ID
 * @property name 名称
 * @property order 顺序
 * @property parentChapterId 父章节ID
 * @property userControlSetTop 是否用户控制置顶
 * @property visible 可见性
 *
 * JSON示例：
 * {
 *     "children": [
 *         {
 *             "children": [],
 *             "courseId": 13,
 *             "id": 60,
 *             "name": "Android Studio相关",
 *             "order": 1000,
 *             "parentChapterId": 150,
 *             "userControlSetTop": false,
 *             "visible": 1
 *         }
 *     ],
 *     "courseId": 13,
 *     "id": 150,
 *     "name": "开发环境",
 *     "order": 1,
 *     "parentChapterId": 0,
 *     "userControlSetTop": false,
 *     "visible": 1
 * }
 */
@Serializable
data class SystemCategory(
    val children: List<SystemCategory>? = null,
    val courseId: Int = 0,
    val id: Int = 0,
    val name: String? = null,
    val order: Int = 0,
    val parentChapterId: Int = 0,
    val userControlSetTop: Boolean = false,
    val visible: Int = 0
)
