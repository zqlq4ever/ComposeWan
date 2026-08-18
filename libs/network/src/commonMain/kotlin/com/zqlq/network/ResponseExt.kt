package com.zqlq.network

import com.zqlq.common.data.response.BaseResponse

/** errorCode 非 0 时抛出 [ApiException]。 */
fun BaseResponse<*>.ensureSuccess() {
    if (errorCode != 0) {
        throw ApiException(errorCode, errorMsg.ifBlank { "request failed" })
    }
}

/** 校验成功并返回 data，data 为空则抛异常。 */
fun <T> BaseResponse<T>.requireData(): T {
    ensureSuccess()
    return data ?: throw ApiException(errorCode, errorMsg.ifBlank { "empty data" })
}
