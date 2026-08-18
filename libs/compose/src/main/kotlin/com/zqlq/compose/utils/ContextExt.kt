package com.zqlq.compose.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/** 从 Context 向上查找 Activity。 */
internal fun Context.findActivity(): Activity? {
    var ctx: Context? = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}
