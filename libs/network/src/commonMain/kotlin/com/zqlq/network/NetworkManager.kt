package com.zqlq.network

import com.zqlq.common.data.response.BaseResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.parameters
import kotlinx.serialization.json.JsonElement

/**
 * 网络请求管理器。
 * GET / 表单 POST 均按玩 Android 的 `{errorCode, errorMsg, data}` 解析。
 */
class NetworkManager(
    val client: HttpClient,
    val config: NetworkConfig,
) {
    fun configure(block: NetworkConfig.() -> Unit) {
        config.apply(block)
    }

    suspend inline fun <reified T> get(
        path: String,
        query: Map<String, Any?> = emptyMap(),
    ): T {
        val response: BaseResponse<T> =
            client
                .get {
                    url("${config.baseUrl}$path")
                    query.forEach { (key, value) ->
                        if (value != null) parameter(key, value.toString())
                    }
                }.body()
        return response.requireData()
    }

    /** GET：成功即可，允许 data 为空。 */
    suspend inline fun <reified T> getAllowEmpty(
        path: String,
        query: Map<String, Any?> = emptyMap(),
    ): T? {
        val response: BaseResponse<T> =
            client
                .get {
                    url("${config.baseUrl}$path")
                    query.forEach { (key, value) ->
                        if (value != null) parameter(key, value.toString())
                    }
                }.body()
        response.ensureSuccess()
        return response.data
    }

    /** 表单 POST，响应 data 可为空（收藏等接口）。 */
    suspend fun postForm(
        path: String,
        form: Map<String, String> = emptyMap(),
        query: Map<String, Any?> = emptyMap(),
    ) {
        val response: BaseResponse<JsonElement?> =
            client
                .submitForm(
                    url = "${config.baseUrl}$path",
                    formParameters =
                        parameters {
                            form.forEach { (key, value) -> append(key, value) }
                        },
                ) {
                    query.forEach { (key, value) ->
                        if (value != null) parameter(key, value.toString())
                    }
                }.body()
        response.ensureSuccess()
    }

    /** 表单 POST 并解析 data。 */
    suspend inline fun <reified T> postFormData(
        path: String,
        form: Map<String, String> = emptyMap(),
        query: Map<String, Any?> = emptyMap(),
    ): T {
        val response: BaseResponse<T> =
            client
                .submitForm(
                    url = "${config.baseUrl}$path",
                    formParameters =
                        parameters {
                            form.forEach { (key, value) -> append(key, value) }
                        },
                ) {
                    query.forEach { (key, value) ->
                        if (value != null) parameter(key, value.toString())
                    }
                }.body()
        return response.requireData()
    }

    companion object {
        private var instance: NetworkManager? = null

        fun initialize(
            client: HttpClient,
            config: NetworkConfig,
        ) {
            instance = NetworkManager(client, config)
        }

        fun getInstance(): NetworkManager =
            instance ?: throw IllegalStateException("NetworkManager has not been initialized")
    }
}
