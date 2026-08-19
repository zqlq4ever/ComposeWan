package com.zqlq.composewan

import android.app.Application
import android.content.res.Configuration
import com.zqlq.common.utils.log.LogUtils
import com.zqlq.common.utils.storage.MMKVUtils
import com.zqlq.common.utils.toast.ToastUtils
import com.zqlq.composewan.di.appModules
import com.zqlq.composewan.ui.settings.LocaleHelper
import com.zqlq.network.AndroidNetworkClient
import com.zqlq.network.NetworkConfig
import com.zqlq.network.NetworkManager
import com.zqlq.network.WanPaths
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

/**
 * 全局 Application
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initToast()
        initLog()
        initMMKV()
        LocaleHelper.applyFromPreferences()
        initNetwork()
        initKoin()
    }

    /**
     * 初始化 Toast 框架
     */
    private fun initToast() {
        val isDarkTheme =
            resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        ToastUtils.init(this, isDarkTheme)
    }

    /**
     * 初始化 Log 框架
     */
    private fun initLog() {
        LogUtils.init(this, BuildConfig.DEBUG)
    }

    /**
     * 初始化 MMKV 框架
     */
    private fun initMMKV() {
        MMKVUtils.init(this)
    }

    /** 初始化玩 Android 网络客户端（Cookie 依赖 MMKV）。 */
    private fun initNetwork() {
        NetworkManager.initialize(
            client = AndroidNetworkClient.create(),
            config = NetworkConfig(baseUrl = WanPaths.BASE_URL, debug = BuildConfig.DEBUG),
        )
    }

    /**
     * 初始化 Koin
     */
    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
    }

    /**
     * 应用配置变化时调用（如切换深色模式）
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isDarkTheme =
            newConfig.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        if (isDarkTheme) {
            ToastUtils.setWhiteStyle()
        } else {
            ToastUtils.setBlackStyle()
        }
    }
}
