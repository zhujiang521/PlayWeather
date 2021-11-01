package com.zj.weather.utils

import android.content.Context
import com.qweather.sdk.bean.base.Lang


/**
 * 获取默认语言
 */
fun getDefaultLocale(context: Context?): Lang {
    if (context == null) return Lang.ZH_HANS
    return when (context.resources.configuration.locale.language) {
        "zh" -> Lang.ZH_HANS
        "zh_HK", "zh_TW" -> Lang.ZH_HANT
        "es", "en" -> Lang.EN
        else -> Lang.EN
    }
}
