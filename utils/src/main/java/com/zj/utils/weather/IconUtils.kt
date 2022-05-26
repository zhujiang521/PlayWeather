package com.zj.utils.weather

import android.content.Context
import com.zj.utils.R
import com.zj.utils.isDarkMode

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
     * 获取天气图标
     *
     * @param weather 天气状况代码
     * @return 天气icon
     */
    fun getWeatherIcon(weather: String?, context: Context? = null): Int {
        if (weather == null) return R.drawable.x_sunny
        var isDay = true
        if (context != null) {
            isDay = !context.isDarkMode()
        }
        return when (weather) {
            "100", "150" -> if (isDay) R.drawable.x_sunny else R.drawable.x_yewan_qing
            "101", "102", "103", "151", "152", "153" -> if (isDay) R.drawable.x_cloud else R.drawable.x_cloudy_night
            "104", "154", "200", "201", "202", "203", "204", "205", "206", "207", "208",
            "209", "210", "211", "212", "213" -> R.drawable.x_overcast
            "300", "301" -> if (isDay) R.drawable.x_shower else R.drawable.x_yejian_zhenyu
            "302", "303", "304" -> R.drawable.x_thunder_rain
            "313" -> R.drawable.x_rain_ice
            "404", "405", "406" -> R.drawable.x_rain_snow
            "305", "308", "309" -> R.drawable.x_small_rain
            "306", "350", "351", "399" -> R.drawable.x_middle_rain
            "307" -> R.drawable.x_big_rain
            "310", "311", "312" -> R.drawable.x_storm
            "456", "457", "499" -> if (isDay) R.drawable.x_snow_flurry else R.drawable.x_snow_flurry_night
            "400", "407", "408" -> R.drawable.x_light_snow
            "401", "409" -> R.drawable.x_moderate_snow
            "402", "410" -> R.drawable.x_heavy_snow
            "403" -> R.drawable.x_snow_storm
            "500", "501", "509", "510", "514", "515" -> R.drawable.x_fog
            "507" -> R.drawable.x_shachebao
            "504" -> R.drawable.x_fuchen
            "503" -> R.drawable.x_yangsha
            "508" -> R.drawable.x_shachebao
            "502", "511", "512", "513" -> R.drawable.x_haze
            else -> R.drawable.x_nodata
        }
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