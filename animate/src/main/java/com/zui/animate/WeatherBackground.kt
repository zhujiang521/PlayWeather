package com.zui.animate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 天气动画类型
 * 目前做了晴天、下雨、下雪及多云
 * 别的极端天气，类似雾霾等等没有做
 */
object WeatherAnimateType {
    // 晴
    const val SUN = 10

    // 小雨
    const val LIGHT_RAIN = 21

    // 中雨
    const val MODERATE_RAIN = 22

    // 大雨
    const val HEAVY_RAIN = 23

    // 暴雨
    const val RAINSTORM = 24

    // 雷雨
    const val THUNDER_RAIN = 25

    // 小雪
    const val LIGHT_SNOW = 31

    // 中雪
    const val MODERATE_SNOW = 32

    // 大雪
    const val HEAVY_SNOW = 33

    // 暴雪
    const val BLIZZARD = 34

    // 多云
    const val CLOUDY = 40
}

/**
 * 根据不同的
 */
@Composable
fun WeatherBackground(
    modifier: Modifier = Modifier,
    weatherIcon: String? = "100",
) {
    when (getWeatherAnimateType(weatherIcon)) {
        WeatherAnimateType.SUN -> {
            Sun()
        }

        WeatherAnimateType.LIGHT_RAIN -> {
            Rain(modifier, Rains.LIGHT_RAIN)
        }

        WeatherAnimateType.MODERATE_RAIN -> {
            Rain(modifier, Rains.MODERATE_RAIN)
        }

        WeatherAnimateType.HEAVY_RAIN -> {
            Rain(modifier, Rains.HEAVY_RAIN)
        }

        WeatherAnimateType.RAINSTORM -> {
            Rain(modifier, Rains.RAINSTORM)
        }

        WeatherAnimateType.THUNDER_RAIN -> {
            Rain(modifier, Rains.MODERATE_RAIN)
            Thunder(modifier)
        }

        WeatherAnimateType.LIGHT_SNOW -> {
            Snow(modifier, Snows.LIGHT_SNOW)
        }

        WeatherAnimateType.MODERATE_SNOW -> {
            Snow(modifier, Snows.MODERATE_SNOW)
        }

        WeatherAnimateType.HEAVY_SNOW -> {
            Snow(modifier, Snows.HEAVY_SNOW)
        }

        WeatherAnimateType.BLIZZARD -> {
            Snow(modifier, Snows.BLIZZARD)
        }

        WeatherAnimateType.CLOUDY -> {
            Cloudy(modifier)
        }
    }
}

/**
 * 获取需要展示的动画
 */
private fun getWeatherAnimateType(weather: String?): Int? {
    return when (weather) {
        "100", "150" -> WeatherAnimateType.SUN

        "101", "102", "103", "151", "152", "153", "104", "154", "200", "201", "202", "203", "204", "205", "206", "207", "208",
        "209", "210", "211", "212", "213" -> WeatherAnimateType.CLOUDY

        "300", "301", "305", "308", "309" -> WeatherAnimateType.LIGHT_RAIN
        "306", "350", "351", "399" -> WeatherAnimateType.MODERATE_RAIN
        "307" -> WeatherAnimateType.HEAVY_RAIN
        "310", "311", "312" -> WeatherAnimateType.RAINSTORM
        "302", "303", "304" -> WeatherAnimateType.THUNDER_RAIN

        "400", "407", "408", "456", "457", "499", "404", "405", "406", "313" -> WeatherAnimateType.LIGHT_SNOW
        "401", "409" -> WeatherAnimateType.MODERATE_SNOW
        "402", "410" -> WeatherAnimateType.HEAVY_SNOW
        "403" -> WeatherAnimateType.BLIZZARD

        else -> null
    }
}