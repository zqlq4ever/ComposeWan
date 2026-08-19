package com.zqlq.composewan.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.zqlq.common.utils.prefs.AppLanguage
import com.zqlq.common.utils.prefs.AppPreferences

/** 按偏好应用系统语言。 */
object LocaleHelper {
    fun applyFromPreferences() {
        apply(AppPreferences.language())
    }

    fun apply(language: AppLanguage) {
        val locales =
            when (language) {
                AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
                AppLanguage.ZH -> LocaleListCompat.forLanguageTags("zh")
                AppLanguage.EN -> LocaleListCompat.forLanguageTags("en")
            }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    fun setAndApply(language: AppLanguage) {
        AppPreferences.setLanguage(language)
        apply(language)
    }
}
