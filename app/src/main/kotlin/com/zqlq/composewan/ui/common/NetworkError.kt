package com.zqlq.composewan.ui.common

import java.net.ConnectException
import java.net.UnknownHostException
import java.nio.channels.UnresolvedAddressException

/** 无网/DNS 失败时写入 UiState.error 的标记，由界面转成资源文案。 */
const val NETWORK_UNAVAILABLE_MARKER = "network_unavailable"

fun Throwable.isOfflineNetworkError(): Boolean {
    var current: Throwable? = this
    while (current != null) {
        when (current) {
            is UnknownHostException,
            is UnresolvedAddressException,
            is ConnectException,
            -> return true
        }
        val name = current::class.simpleName.orEmpty()
        if (name.contains("UnresolvedAddress", ignoreCase = true)) {
            return true
        }
        current = current.cause
    }
    return false
}

fun Throwable.toLoadError(): String = if (isOfflineNetworkError()) NETWORK_UNAVAILABLE_MARKER else message.orEmpty()
