package com.zqlq.network

import com.zqlq.common.data.response.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * 网络请求管理器
 * 支持切换baseurl，封装常用请求方法
 */
class NetworkManager private constructor(
    private val client: HttpClient,
    private var config: NetworkConfig
) {
    /**
     * 配置网络请求
     */
    fun configure(block: NetworkConfig.() -> Unit) {
        config.apply(block)
    }

    /**
     * 发送GET请求
     * @param path 请求路径
     * @return BaseResponse<T>
     */
    suspend fun <T> get(path: String): BaseResponse<T> {
        return client.get {
            url("${config.baseUrl}$path")
        }.body()
    }

    /**
     * 发送POST请求
     * @param path 请求路径
     * @param body 请求体
     * @return BaseResponse<T>
     */
    suspend fun <T> post(path: String, body: Any): BaseResponse<T> {
        return client.post {
            url("${config.baseUrl}$path")
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    companion object {
        private var instance: NetworkManager? = null

        /**
         * 初始化网络管理器
         * @param client HttpClient实例
         * @param config 网络配置
         */
        fun initialize(client: HttpClient, config: NetworkConfig) {
            instance = NetworkManager(client, config)
        }

        /**
         * 获取网络管理器实例
         */
        fun getInstance(): NetworkManager {
            return instance ?: throw IllegalStateException("NetworkManager has not been initialized")
        }
    }
}
