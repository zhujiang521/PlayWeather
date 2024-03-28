package com.zj.weather.view.season

import java.util.Calendar


enum class Season {
    SPRING, // 春天
    SUMMER, // 夏天
    FALL, // 秋天
    WINTER // 冬天
}

/**
 * 获取季节
 */
internal fun getSeason(): Season {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.MONTH)) {
        3, 4, 5 -> Season.SPRING
        6, 7, 8 -> Season.SUMMER
        9, 10, 11 -> Season.FALL
        else -> Season.WINTER
    }
}
