package com.zqlq.common.data.response

import kotlinx.serialization.Serializable

/**
 * API通用返回数据结构
 * @param T 泛型数据类型
 * @property data 返回的数据
 * @property errorCode 错误码：0成功，-1001登录失效，其他为错误
 * @property errorMsg 错误信息
 *
 * JSON示例：
 * {
 *     "data": ...,
 *     "errorCode": 0,
 *     "errorMsg": ""
 * }
 */
@Serializable
data class BaseResponse<T>(
    val data: T? = null,
    val errorCode: Int = 0,
    val errorMsg: String = ""
)
