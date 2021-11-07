package com.zj.weather.utils

import android.content.Context
import com.qweather.sdk.bean.base.Lang


/**
 * 获取默认语言
 */
fun getDefaultLocale(context: Context?): Lang {
    if (context == null) return Lang.ZH_HANS
    Xlog.d("getDefaultLocale: ${context.resources.configuration.locales[0].toLanguageTag()}")
    return when (context.resources.configuration.locales[0].toLanguageTag()) {
        "zh", "zh-CN" -> Lang.ZH_HANS
        "zh_rHK", "zh_rTW", "zh_HK", "zh_TW", "HK", "TW", "zh-TW", "zh-HK" -> Lang.ZH_HANT
        "es", "en" -> Lang.EN
        else -> Lang.EN
    }
}
