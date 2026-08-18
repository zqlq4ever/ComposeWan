package com.zqlq.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/** Android 平台网络客户端：Cookie 持久化 + JSON。 */
object AndroidNetworkClient {
    fun create(): HttpClient =
        HttpClient(Android) {
            install(HttpTimeout) {
                connectTimeoutMillis = 30_000
                requestTimeoutMillis = 30_000
                socketTimeoutMillis = 30_000
            }
            install(HttpCookies) {
                storage = MmkvCookiesStorage()
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        prettyPrint = false
                    },
                )
            }
            install(Logging) {
                level = LogLevel.INFO
                logger =
                    object : Logger {
                        override fun log(message: String) {
                            println("[Network] $message")
                        }
                    }
            }
        }
}
