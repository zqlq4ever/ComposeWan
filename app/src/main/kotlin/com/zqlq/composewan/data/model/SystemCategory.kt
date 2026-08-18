package com.zqlq.composewan.data.model

import kotlinx.serialization.Serializable

/**
 * 体系分类数据类
 * @param id 标识
 * @param name 分类名称
 * @param children 子分类列表
 */
@Serializable
data class SystemCategory(
    val id: Int,
    val name: String,
    val children: List<SystemChild>,
)

/**
 * 体系子分类数据类
 * @param id 标识
 * @param name 子分类名称
 */
@Serializable
data class SystemChild(
    val id: Int,
    val name: String,
)
