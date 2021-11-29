package com.zj.weather.utils.weather

import android.content.Context
import com.zj.weather.R
import com.zj.weather.utils.isDarkMode

object IconUtils {

    /**
     * 获取天气动画
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    fun getWeatherAnimationIcon(context: Context?, weather: String?): Int {
        if (context == null || weather == null) return R.raw.weather_sun
        return if (context.isDarkMode()) {
            getNightAnimationIcon(weather)
        } else {
            getDayAnimationIcon(weather)
        }
    }

    /**
     * 晚上动画
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    private fun getNightAnimationIcon(weather: String): Int {
        val imageId: Int = when (weather) {
            "100", "900", "901", "999" -> R.raw.weather_moon
            "101", "103" -> R.raw.weather_moon_cloudy
            "102", "104" -> R.raw.weather_cloudy
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213" -> R.raw.weather_mist
            "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515" -> R.raw.weather_foggy
            "300", "301", "302", "399", "305", "306", "307", "308", "309", "310" -> R.raw.weather_moon_rain
            "303" -> R.raw.weather_stormshowersday
            "304" -> R.raw.weather_thunder
            "311", "312", "313", "314", "315", "316", "317", "318" -> R.raw.weather_thunder_rain
            "400", "401", "402", "403", "404", "405" -> R.raw.weather_snow
            "406", "407", "408", "409", "410", "499" -> R.raw.weather_moon_snows
            else -> R.raw.weather_moon
        }
        return imageId
    }

    /**
     * 白天动画
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    private fun getDayAnimationIcon(weather: String): Int {
        val imageId: Int = when (weather) {
            "100", "900", "901", "999" -> R.raw.weather_sun
            "101", "103" -> R.raw.weather_sun_cloudy
            "102", "104" -> R.raw.weather_cloudy
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213" -> R.raw.weather_mist
            "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515" -> R.raw.weather_foggy
            "300", "301", "302", "399", "305", "306", "307", "308", "309", "310" -> R.raw.weather_rain
            "303" -> R.raw.weather_stormshowersday
            "304" -> R.raw.weather_thunder
            "311", "312", "313", "314", "315", "316", "317", "318" -> R.raw.weather_thunder_rain
            "400", "401", "402", "403", "404", "405" -> R.raw.weather_snow
            "406", "407", "408", "409", "410", "499" -> R.raw.weather_sun_snows
            else -> R.raw.weather_sun
        }
        return imageId
    }

    /**
     * 获取天气图标
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    fun getWeatherIcon(weather: String?): Int {
        if (weather == null) return R.drawable.icon_100
        return getDayIconDark(weather)
    }

    /**
     * 获取天气背景
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    fun getWeatherBack(context: Context?, weather: String?): Int {
        if (context == null || weather == null) return R.mipmap.back_100d
        return if (context.isDarkMode()) {
            getNightBack(weather)
        } else {
            getDayBack(weather)
        }
    }

    /**
     * 获取白天天气图标
     */
    private fun getDayIconDark(weather: String?): Int {
        val imageId: Int
        when (weather) {
            "100" -> imageId = R.drawable.icon_100
            "101" -> imageId = R.drawable.icon_101
            "102" -> imageId = R.drawable.icon_102
            "103" -> imageId = R.drawable.icon_103
            "104" -> imageId = R.drawable.icon_104
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213" ->
                imageId = R.mipmap.icon_200n
            "300" -> imageId = R.drawable.icon_300
            "301" -> imageId = R.drawable.icon_301
            "302" -> imageId = R.drawable.icon_302
            "303" -> imageId = R.drawable.icon_303
            "304" -> imageId = R.drawable.icon_304
            "305" -> imageId = R.drawable.icon_305
            "306" -> imageId = R.drawable.icon_306
            "307" -> imageId = R.drawable.icon_307
            "308" -> imageId = R.drawable.icon_308
            "309" -> imageId = R.drawable.icon_309
            "310" -> imageId = R.drawable.icon_310
            "311" -> imageId = R.drawable.icon_311
            "312" -> imageId = R.drawable.icon_312
            "313" -> imageId = R.drawable.icon_313
            "314" -> imageId = R.drawable.icon_314
            "315" -> imageId = R.drawable.icon_315
            "316" -> imageId = R.drawable.icon_316
            "317" -> imageId = R.drawable.icon_317
            "318" -> imageId = R.drawable.icon_318
            "399" -> imageId = R.drawable.icon_399
            "400" -> imageId = R.drawable.icon_400
            "401" -> imageId = R.drawable.icon_401
            "402" -> imageId = R.drawable.icon_402
            "403" -> imageId = R.drawable.icon_403
            "404" -> imageId = R.drawable.icon_404
            "405" -> imageId = R.drawable.icon_405
            "406" -> imageId = R.drawable.icon_406
            "407" -> imageId = R.drawable.icon_407
            "408" -> imageId = R.drawable.icon_408
            "409" -> imageId = R.drawable.icon_409
            "410" -> imageId = R.drawable.icon_410
            "499" -> imageId = R.drawable.icon_499
            "500" -> imageId = R.drawable.icon_500
            "501" -> imageId = R.drawable.icon_501
            "502" -> imageId = R.drawable.icon_502
            "503" -> imageId = R.drawable.icon_503
            "504" -> imageId = R.drawable.icon_504
            "507" -> imageId = R.drawable.icon_507
            "508" -> imageId = R.drawable.icon_508
            "509" -> imageId = R.drawable.icon_509
            "510" -> imageId = R.drawable.icon_510
            "511" -> imageId = R.drawable.icon_511
            "512" -> imageId = R.drawable.icon_512
            "513" -> imageId = R.drawable.icon_513
            "514" -> imageId = R.drawable.icon_514
            "515" -> imageId = R.drawable.icon_515
            "900" -> imageId = R.drawable.icon_900
            "901" -> imageId = R.drawable.icon_901
            "999" -> imageId = R.drawable.icon_999
            else -> imageId = R.drawable.icon_100
        }
        return imageId
    }

    /**
     * 获取白天背景
     */
    private fun getDayBack(weather: String?): Int {
        val imageId: Int
        when (weather) {
            "100", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213" -> imageId =
                R.mipmap.back_100d
            "101", "102", "103" -> imageId = R.mipmap.back_101d
            "104" -> imageId = R.mipmap.back_104d
            "300", "301", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "399" ->
                imageId = R.mipmap.back_300d
            "302", "303", "304" -> imageId = R.mipmap.back_302d
            "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "499" ->
                imageId = R.mipmap.back_400d
            "500" -> imageId = R.mipmap.back_500d
            "501" -> imageId = R.mipmap.back_501d
            "502" -> imageId = R.mipmap.back_502d
            "503", "504", "507", "508" -> imageId = R.mipmap.back_503d
            "509", "510", "514", "515" -> imageId = R.mipmap.back_509d
            "511", "512", "513" -> imageId = R.mipmap.back_511d
            "900" -> imageId = R.mipmap.back_900d
            "901" -> imageId = R.mipmap.back_901d
            else -> imageId = R.mipmap.back_100d
        }
        return imageId
    }

    /**
     * 获取晚上背景
     */
    private fun getNightBack(weather: String?): Int {
        val imageId: Int
        when (weather) {
            "100", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213" ->
                imageId = R.mipmap.back_100n
            "101", "102", "103" -> imageId = R.mipmap.back_101n
            "104" -> imageId = R.mipmap.back_104n
            "300", "301", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "399" ->
                imageId = R.mipmap.back_300n
            "302", "303", "304" -> imageId = R.mipmap.back_302n
            "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "499" ->
                imageId = R.mipmap.back_400n
            "500" -> imageId = R.mipmap.back_500n
            "501" -> imageId = R.mipmap.back_501n
            "502" -> imageId = R.mipmap.back_502n
            "503", "504", "507", "508" -> imageId = R.mipmap.back_503n
            "509", "510", "514", "515" -> imageId = R.mipmap.back_509n
            "511", "512", "513" -> imageId = R.mipmap.back_511n
            "900" -> imageId = R.mipmap.back_900n
            else -> imageId = R.mipmap.back_100n
        }
        return imageId
    }
}