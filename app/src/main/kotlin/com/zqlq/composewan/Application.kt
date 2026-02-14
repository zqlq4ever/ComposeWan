package com.zqlq.composewan

import android.app.Application
import android.content.res.Configuration
import com.zqlq.common.utils.log.LogUtils
import com.zqlq.common.utils.storage.MMKVUtils
import com.zqlq.common.utils.toast.ToastUtils

/**
 * 全局 Application
 */
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initToast()
        initLog()
        initMMKV()
    }

    /**
     * 初始化 Toast 框架
     */
    private fun initToast() {
        // 检测当前是否为深色模式
        val isDarkTheme = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        // 初始化Toast，传递深色模式参数
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

    /**
     * 应用配置变化时调用（如切换深色模式）
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        // 检测深色模式变化并更新Toast样式
        val isDarkTheme = newConfig.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

        // 根据当前主题重新设置Toast样式
        if (isDarkTheme) {
            ToastUtils.setWhiteStyle()
        } else {
            ToastUtils.setBlackStyle()
        }
    }
}