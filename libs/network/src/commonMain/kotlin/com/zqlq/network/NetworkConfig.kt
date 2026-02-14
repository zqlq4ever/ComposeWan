package com.zqlq.network

/**
 * 网络请求配置类
 * @property baseUrl 基础URL
 * @property debug 是否开启调试模式
 */
data class NetworkConfig(
    var baseUrl: String,
    var debug: Boolean = true
)
