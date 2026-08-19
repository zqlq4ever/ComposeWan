package com.zqlq.network

import com.zqlq.common.utils.storage.MMKVUtils
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

/** 将玩 Android 登录 Cookie 持久化到 MMKV。 */
class MmkvCookiesStorage : CookiesStorage {
    private val mutex = Mutex()
    private val json =
        Json {
            ignoreUnknownKeys = true
        }
    private val serializer = ListSerializer(StoredCookie.serializer())

    override suspend fun addCookie(
        requestUrl: Url,
        cookie: Cookie,
    ) {
        mutex.withLock {
            val domain = cookie.domain?.removePrefix(".") ?: requestUrl.host
            val path = cookie.path ?: "/"
            val stored =
                load().filterNot { it.name == cookie.name && it.domain.equals(domain, ignoreCase = true) } +
                    StoredCookie(
                        name = cookie.name,
                        value = cookie.value,
                        domain = domain,
                        path = path,
                        secure = cookie.secure,
                        httpOnly = cookie.httpOnly,
                    )
            MMKVUtils.putString(KEY_COOKIES, json.encodeToString(serializer, stored))
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> =
        mutex.withLock {
            load()
                .filter { requestUrl.host.endsWith(it.domain.removePrefix("."), ignoreCase = true) }
                .map { item ->
                    Cookie(
                        name = item.name,
                        value = item.value,
                        domain = item.domain,
                        path = item.path,
                        secure = item.secure,
                        httpOnly = item.httpOnly,
                    )
                }
        }

    override fun close() = Unit

    /** 清空本地登录 Cookie。 */
    suspend fun clear() {
        mutex.withLock {
            MMKVUtils.remove(KEY_COOKIES)
        }
    }

    private fun load(): List<StoredCookie> {
        val raw = MMKVUtils.getString(KEY_COOKIES, "")
        if (raw.isBlank()) return emptyList()
        return runCatching { json.decodeFromString(serializer, raw) }.getOrDefault(emptyList())
    }

    companion object {
        private const val KEY_COOKIES = "wan_cookies"

        /** 不依赖实例，直接清 MMKV 中的 Cookie。 */
        fun clearStored() {
            MMKVUtils.remove(KEY_COOKIES)
        }
    }
}

@Serializable
internal data class StoredCookie(
    val name: String,
    val value: String,
    val domain: String,
    val path: String,
    val secure: Boolean,
    val httpOnly: Boolean,
)
