package com.zqlq.common.utils.log

import android.app.Application
import android.util.Log
import timber.log.Timber

/**
 * 日志工具类，基于 Timber 框架封装
 * 提供统一的日志记录接口，支持不同级别的日志输�?
 *

 */
object LogUtils {

    /**
     * 是否启用调试模式
     */
    private var isDebugMode = false

    /**
     * 初始化日志框架，应在 Application 中调�?
     * 用法示例：LogUtils.init(application, BuildConfig.DEBUG)
     *
     * @param application Application 对象
     * @param isDebug 是否为调试模式，用于决定是否植入调试�?

     */
    fun init(application: Application, isDebug: Boolean = false) {
        isDebugMode = isDebug

        if (isDebug) {
            // 调试模式下植�?DebugTree，自动标记调用类名作为标�?
            Timber.plant(Timber.DebugTree())
        } else {
            // 可以在这里植入自定义的发布版日志树（例如崩溃上报�?
            // Timber.plant(CrashReportingTree())
        }
    }

    /**
     * 植入自定义日志树
     * 用法示例：LogUtils.plant(MyCustomTree())
     *
     * @param tree Timber.Tree 的实�?

     */
    fun plant(tree: Timber.Tree) {
        Timber.plant(tree)
    }

    /**
     * 移除所有日志树
     * 用法示例：LogUtils.clearLogs()
     *

     */
    fun clearLogs() {
        Timber.uprootAll()
    }

    /**
     * 移除指定的日志树
     * 用法示例：LogUtils.removeTree(myTree)
     *
     * @param tree 要移除的 Timber.Tree 实例

     */
    fun removeTree(tree: Timber.Tree) {
        Timber.uproot(tree)
    }

    // ==================== 带TAG的日志方�?====================

    /**
     * 记录 VERBOSE 级别日志，带TAG
     * 用法示例：LogUtils.v(TAG, "消息内容")
     *
     * @param tag 日志标签
     * @param message 日志消息

     */
    fun v(tag: String, message: String) {
        Log.v(tag, message)
    }

    /**
     * 记录 DEBUG 级别日志，带TAG
     * 用法示例：LogUtils.d(TAG, "消息内容")
     *
     * @param tag 日志标签
     * @param message 日志消息

     */
    fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    /**
     * 记录 INFO 级别日志，带TAG
     * 用法示例：LogUtils.i(TAG, "消息内容")
     *
     * @param tag 日志标签
     * @param message 日志消息

     */
    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }

    /**
     * 记录 WARN 级别日志，带TAG
     * 用法示例：LogUtils.w(TAG, "消息内容")
     *
     * @param tag 日志标签
     * @param message 日志消息

     */
    fun w(tag: String, message: String) {
        Log.w(tag, message)
    }

    /**
     * 记录 ERROR 级别日志，带TAG
     * 用法示例：LogUtils.e(TAG, "消息内容")
     *
     * @param tag 日志标签
     * @param message 日志消息

     */
    fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

    /**
     * 记录 ERROR 级别日志，带TAG和异�?
     * 用法示例：LogUtils.e(TAG, "消息内容", throwable)
     *
     * @param tag 日志标签
     * @param message 日志消息
     * @param t 异常对象

     */
    fun e(tag: String, message: String, t: Throwable) {
        Log.e(tag, message, t)
    }

    // ==================== 原有日志方法 ====================

    /**
     * 记录 VERBOSE 级别日志
     * 用法示例：LogUtils.v("消息内容")
     *
     * @param message 日志消息

     */
    fun v(message: String) {
        Timber.v(message)
    }

    /**
     * 记录 VERBOSE 级别日志，带格式化参�?
     * 用法示例：LogUtils.v("用户 %s 登录成功", username)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun v(message: String, vararg args: Any?) {
        Timber.v(message, *args)
    }

    /**
     * 记录 VERBOSE 级别日志，带异常
     * 用法示例：LogUtils.v(exception, "发生异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun v(t: Throwable, message: String) {
        Timber.v(t, message)
    }

    /**
     * 记录 VERBOSE 级别日志，带异常和格式化参数
     * 用法示例：LogUtils.v(exception, "用户 %s 操作发生异常", username)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun v(t: Throwable, message: String, vararg args: Any?) {
        Timber.v(t, message, *args)
    }

    /**
     * 记录 DEBUG 级别日志
     * 用法示例：LogUtils.d("消息内容")
     *
     * @param message 日志消息

     */
    fun d(message: String) {
        Timber.d(message)
    }

    /**
     * 记录 DEBUG 级别日志，带格式化参�?
     * 用法示例：LogUtils.d("用户 %s 登录成功", username)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun d(message: String, vararg args: Any?) {
        Timber.d(message, *args)
    }

    /**
     * 记录 DEBUG 级别日志，带异常
     * 用法示例：LogUtils.d(exception, "发生异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun d(t: Throwable, message: String) {
        Timber.d(t, message)
    }

    /**
     * 记录 DEBUG 级别日志，带异常和格式化参数
     * 用法示例：LogUtils.d(exception, "用户 %s 操作发生异常", username)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun d(t: Throwable, message: String, vararg args: Any?) {
        Timber.d(t, message, *args)
    }

    /**
     * 记录 INFO 级别日志
     * 用法示例：LogUtils.i("消息内容")
     *
     * @param message 日志消息

     */
    fun i(message: String) {
        Timber.i(message)
    }

    /**
     * 记录 INFO 级别日志，带格式化参�?
     * 用法示例：LogUtils.i("用户 %s 登录成功", username)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun i(message: String, vararg args: Any?) {
        Timber.i(message, *args)
    }

    /**
     * 记录 INFO 级别日志，带异常
     * 用法示例：LogUtils.i(exception, "发生异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun i(t: Throwable, message: String) {
        Timber.i(t, message)
    }

    /**
     * 记录 INFO 级别日志，带异常和格式化参数
     * 用法示例：LogUtils.i(exception, "用户 %s 操作发生异常", username)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun i(t: Throwable, message: String, vararg args: Any?) {
        Timber.i(t, message, *args)
    }

    /**
     * 记录 WARN 级别日志
     * 用法示例：LogUtils.w("消息内容")
     *
     * @param message 日志消息

     */
    fun w(message: String) {
        Timber.w(message)
    }

    /**
     * 记录 WARN 级别日志，带格式化参�?
     * 用法示例：LogUtils.w("用户 %s 登录警告", username)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun w(message: String, vararg args: Any?) {
        Timber.w(message, *args)
    }

    /**
     * 记录 WARN 级别日志，带异常
     * 用法示例：LogUtils.w(exception, "发生异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun w(t: Throwable, message: String) {
        Timber.w(t, message)
    }

    /**
     * 记录 WARN 级别日志，带异常和格式化参数
     * 用法示例：LogUtils.w(exception, "用户 %s 操作发生警告", username)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun w(t: Throwable, message: String, vararg args: Any?) {
        Timber.w(t, message, *args)
    }

    /**
     * 记录 ERROR 级别日志
     * 用法示例：LogUtils.e("错误消息")
     *
     * @param message 日志消息

     */
    fun e(message: String) {
        Timber.e(message)
    }

    /**
     * 记录 ERROR 级别日志，带格式化参�?
     * 用法示例：LogUtils.e("用户 %s 登录失败", username)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun e(message: String, vararg args: Any?) {
        Timber.e(message, *args)
    }

    /**
     * 记录 ERROR 级别日志，带异常
     * 用法示例：LogUtils.e(exception, "发生异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun e(t: Throwable, message: String) {
        Timber.e(t, message)
    }

    /**
     * 记录 ERROR 级别日志，带异常和格式化参数
     * 用法示例：LogUtils.e(exception, "用户 %s 操作发生错误", username)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun e(t: Throwable, message: String, vararg args: Any?) {
        Timber.e(t, message, *args)
    }

    /**
     * 记录致命级别日志（ASSERT�?
     * 用法示例：LogUtils.wtf("严重错误消息")
     *
     * @param message 日志消息

     */
    fun wtf(message: String) {
        Timber.wtf(message)
    }

    /**
     * 记录致命级别日志（ASSERT），带格式化参数
     * 用法示例：LogUtils.wtf("系统 %s 完全崩溃", module)
     *
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun wtf(message: String, vararg args: Any?) {
        Timber.wtf(message, *args)
    }

    /**
     * 记录致命级别日志（ASSERT），带异�?
     * 用法示例：LogUtils.wtf(exception, "发生严重异常")
     *
     * @param t 异常对象
     * @param message 日志消息

     */
    fun wtf(t: Throwable, message: String) {
        Timber.wtf(t, message)
    }

    /**
     * 记录致命级别日志（ASSERT），带异常和格式化参�?
     * 用法示例：LogUtils.wtf(exception, "模块 %s 发生致命错误", module)
     *
     * @param t 异常对象
     * @param message 日志消息，可包含格式化占位符
     * @param args 格式化参�?

     */
    fun wtf(t: Throwable, message: String, vararg args: Any?) {
        Timber.wtf(t, message, *args)
    }

    /**
     * 示例实现：一个可用于生产环境的日志树，将错误日志上报到崩溃分析服�?
     * 可根据实际需求替换为您使用的崩溃上报服务（如Bugly, Firebase Crashlytics等）
     *

     */
    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO) {
                // 低级别日志在生产环境不记�?
                return
            }

            // 只上报错误和警告日志
            if (priority == Log.ERROR || priority == Log.WARN) {
                // 这里可以替换为实际的崩溃上报实现
                // 例如：Crashlytics.log(priority, tag, message)

                if (t != null) {
                    // 上报异常
                    // 例如：Crashlytics.logException(t)
                }
            }
        }
    }
} 
