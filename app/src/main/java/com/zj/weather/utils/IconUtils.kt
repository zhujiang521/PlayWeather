package com.zj.weather.utils

import android.content.Context
import com.zj.weather.R

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
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213",
            "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515" -> R.raw.weather_foggy
            "300", "301", "302", "303", "399" -> R.raw.weather_moon_rain
            "304" -> R.raw.weather_thunder
            "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318" -> R.raw.weather_thunder_rain
            "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "499" -> R.raw.weather_moon_snows
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
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213",
            "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510", "511", "512", "513", "514", "515" -> R.raw.weather_foggy
            "300", "301", "302", "303", "399" -> R.raw.weather_rain
            "304" -> R.raw.weather_thunder
            "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318" -> R.raw.weather_thunder_rain
            "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "499" -> R.raw.weather_sun_snows
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
    fun getWeatherIcon(context: Context?, weather: String?): Int {
        if (context == null || weather == null) return R.mipmap.icon_100d
        return if (context.isDarkMode()) {
            getNightIconDark(weather)
        } else {
            getDayIconDark(weather)
        }
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
            "100" -> imageId = R.mipmap.icon_100d
            "101" -> imageId = R.mipmap.icon_101d
            "102" -> imageId = R.mipmap.icon_102d
            "103" -> imageId = R.mipmap.icon_103d
            "104" -> imageId = R.mipmap.icon_104d
            "200" -> imageId = R.mipmap.icon_200d
            "201" -> imageId = R.mipmap.icon_210d
            "202" -> imageId = R.mipmap.icon_202d
            "203" -> imageId = R.mipmap.icon_203d
            "204" -> imageId = R.mipmap.icon_204d
            "205" -> imageId = R.mipmap.icon_205d
            "206" -> imageId = R.mipmap.icon_206d
            "207" -> imageId = R.mipmap.icon_207d
            "208" -> imageId = R.mipmap.icon_208d
            "209" -> imageId = R.mipmap.icon_209d
            "210" -> imageId = R.mipmap.icon_210d
            "211" -> imageId = R.mipmap.icon_211d
            "212" -> imageId = R.mipmap.icon_212d
            "213" -> imageId = R.mipmap.icon_213d
            "300" -> imageId = R.mipmap.icon_300d
            "301" -> imageId = R.mipmap.icon_301d
            "302" -> imageId = R.mipmap.icon_302d
            "303" -> imageId = R.mipmap.icon_303d
            "304" -> imageId = R.mipmap.icon_304d
            "305" -> imageId = R.mipmap.icon_305d
            "306" -> imageId = R.mipmap.icon_306d
            "307" -> imageId = R.mipmap.icon_307d
            "308" -> imageId = R.mipmap.icon_308d
            "309" -> imageId = R.mipmap.icon_309d
            "310" -> imageId = R.mipmap.icon_310d
            "311" -> imageId = R.mipmap.icon_311d
            "312" -> imageId = R.mipmap.icon_312d
            "313" -> imageId = R.mipmap.icon_313d
            "314" -> imageId = R.mipmap.icon_314d
            "315" -> imageId = R.mipmap.icon_315d
            "316" -> imageId = R.mipmap.icon_316d
            "317" -> imageId = R.mipmap.icon_317d
            "318" -> imageId = R.mipmap.icon_318d
            "399" -> imageId = R.mipmap.icon_399d
            "400" -> imageId = R.mipmap.icon_400d
            "401" -> imageId = R.mipmap.icon_401d
            "402" -> imageId = R.mipmap.icon_402d
            "403" -> imageId = R.mipmap.icon_403d
            "404" -> imageId = R.mipmap.icon_404d
            "405" -> imageId = R.mipmap.icon_405d
            "406" -> imageId = R.mipmap.icon_406d
            "407" -> imageId = R.mipmap.icon_407d
            "408" -> imageId = R.mipmap.icon_408d
            "409" -> imageId = R.mipmap.icon_409d
            "410" -> imageId = R.mipmap.icon_410d
            "499" -> imageId = R.mipmap.icon_499d
            "500" -> imageId = R.mipmap.icon_500d
            "501" -> imageId = R.mipmap.icon_501d
            "502" -> imageId = R.mipmap.icon_502d
            "503" -> imageId = R.mipmap.icon_503d
            "504" -> imageId = R.mipmap.icon_504d
            "507" -> imageId = R.mipmap.icon_507d
            "508" -> imageId = R.mipmap.icon_508d
            "509" -> imageId = R.mipmap.icon_509d
            "510" -> imageId = R.mipmap.icon_510d
            "511" -> imageId = R.mipmap.icon_511d
            "512" -> imageId = R.mipmap.icon_512d
            "513" -> imageId = R.mipmap.icon_513d
            "514" -> imageId = R.mipmap.icon_514d
            "515" -> imageId = R.mipmap.icon_515d
            "900" -> imageId = R.mipmap.icon_900d
            "901" -> imageId = R.mipmap.icon_901d
            "999" -> imageId = R.mipmap.icon_999d
            else -> imageId = R.mipmap.icon_100d
        }
        return imageId
    }

    /**
     * 获取晚上天气图标
     */
    private fun getNightIconDark(weather: String?): Int {
        val imageId: Int
        when (weather) {
            "100" -> imageId = R.mipmap.icon_100n
            "101" -> imageId = R.mipmap.icon_101n
            "102" -> imageId = R.mipmap.icon_102n
            "103" -> imageId = R.mipmap.icon_103n
            "104" -> imageId = R.mipmap.icon_104n
            "200" -> imageId = R.mipmap.icon_200n
            "201" -> imageId = R.mipmap.icon_210n
            "202" -> imageId = R.mipmap.icon_202n
            "203" -> imageId = R.mipmap.icon_203n
            "204" -> imageId = R.mipmap.icon_204n
            "205" -> imageId = R.mipmap.icon_205n
            "206" -> imageId = R.mipmap.icon_206n
            "207" -> imageId = R.mipmap.icon_207n
            "208" -> imageId = R.mipmap.icon_208n
            "209" -> imageId = R.mipmap.icon_209n
            "210" -> imageId = R.mipmap.icon_210n
            "211" -> imageId = R.mipmap.icon_211n
            "212" -> imageId = R.mipmap.icon_212n
            "213" -> imageId = R.mipmap.icon_213n
            "300" -> imageId = R.mipmap.icon_300n
            "301" -> imageId = R.mipmap.icon_301n
            "302" -> imageId = R.mipmap.icon_302n
            "303" -> imageId = R.mipmap.icon_303n
            "304" -> imageId = R.mipmap.icon_304n
            "305" -> imageId = R.mipmap.icon_305n
            "306" -> imageId = R.mipmap.icon_306n
            "307" -> imageId = R.mipmap.icon_307n
            "308" -> imageId = R.mipmap.icon_308n
            "309" -> imageId = R.mipmap.icon_309n
            "310" -> imageId = R.mipmap.icon_310n
            "311" -> imageId = R.mipmap.icon_311n
            "312" -> imageId = R.mipmap.icon_312n
            "313" -> imageId = R.mipmap.icon_313n
            "314" -> imageId = R.mipmap.icon_314n
            "315" -> imageId = R.mipmap.icon_315n
            "316" -> imageId = R.mipmap.icon_316n
            "317" -> imageId = R.mipmap.icon_317n
            "318" -> imageId = R.mipmap.icon_318n
            "399" -> imageId = R.mipmap.icon_399n
            "400" -> imageId = R.mipmap.icon_400n
            "401" -> imageId = R.mipmap.icon_401n
            "402" -> imageId = R.mipmap.icon_402n
            "403" -> imageId = R.mipmap.icon_403n
            "404" -> imageId = R.mipmap.icon_404n
            "405" -> imageId = R.mipmap.icon_405n
            "406" -> imageId = R.mipmap.icon_406n
            "407" -> imageId = R.mipmap.icon_407n
            "408" -> imageId = R.mipmap.icon_408n
            "409" -> imageId = R.mipmap.icon_409n
            "410" -> imageId = R.mipmap.icon_410n
            "499" -> imageId = R.mipmap.icon_499n
            "500" -> imageId = R.mipmap.icon_500n
            "501" -> imageId = R.mipmap.icon_501n
            "502" -> imageId = R.mipmap.icon_502n
            "503" -> imageId = R.mipmap.icon_503n
            "504" -> imageId = R.mipmap.icon_504n
            "507" -> imageId = R.mipmap.icon_507n
            "508" -> imageId = R.mipmap.icon_508n
            "509" -> imageId = R.mipmap.icon_509n
            "510" -> imageId = R.mipmap.icon_510n
            "511" -> imageId = R.mipmap.icon_511n
            "512" -> imageId = R.mipmap.icon_512n
            "513" -> imageId = R.mipmap.icon_513n
            "514" -> imageId = R.mipmap.icon_514n
            "515" -> imageId = R.mipmap.icon_515n
            "900" -> imageId = R.mipmap.icon_900n
            "901" -> imageId = R.mipmap.icon_901n
            "999" -> imageId = R.mipmap.icon_999n
            else -> imageId = R.mipmap.icon_100n
        }
        return imageId
    }

    /**
     * 获取白天背景
     */
    private fun getDayBack(weather: String?): Int {
        val imageId: Int
        when (weather) {
            "100" -> imageId = R.mipmap.back_100d
            "101" -> imageId = R.mipmap.back_101d
            "102" -> imageId = R.mipmap.back_102d
            "103" -> imageId = R.mipmap.back_103d
            "104" -> imageId = R.mipmap.back_104d
            "200" -> imageId = R.mipmap.back_200d
            "201" -> imageId = R.mipmap.back_210d
            "202" -> imageId = R.mipmap.back_202d
            "203" -> imageId = R.mipmap.back_203d
            "204" -> imageId = R.mipmap.back_204d
            "205" -> imageId = R.mipmap.back_205d
            "206" -> imageId = R.mipmap.back_206d
            "207" -> imageId = R.mipmap.back_207d
            "208" -> imageId = R.mipmap.back_208d
            "209" -> imageId = R.mipmap.back_209d
            "210" -> imageId = R.mipmap.back_210d
            "211" -> imageId = R.mipmap.back_211d
            "212" -> imageId = R.mipmap.back_212d
            "213" -> imageId = R.mipmap.back_213d
            "300" -> imageId = R.mipmap.back_300d
            "301" -> imageId = R.mipmap.back_301d
            "302" -> imageId = R.mipmap.back_302d
            "303" -> imageId = R.mipmap.back_303d
            "304" -> imageId = R.mipmap.back_304d
            "305" -> imageId = R.mipmap.back_305d
            "306" -> imageId = R.mipmap.back_306d
            "307" -> imageId = R.mipmap.back_307d
            "308" -> imageId = R.mipmap.back_308d
            "309" -> imageId = R.mipmap.back_309d
            "310" -> imageId = R.mipmap.back_310d
            "311" -> imageId = R.mipmap.back_311d
            "312" -> imageId = R.mipmap.back_312d
            "313" -> imageId = R.mipmap.back_313d
            "314" -> imageId = R.mipmap.back_314d
            "315" -> imageId = R.mipmap.back_315d
            "316" -> imageId = R.mipmap.back_316d
            "317" -> imageId = R.mipmap.back_317d
            "318" -> imageId = R.mipmap.back_318d
            "399" -> imageId = R.mipmap.back_399d
            "400" -> imageId = R.mipmap.back_400d
            "401" -> imageId = R.mipmap.back_401d
            "402" -> imageId = R.mipmap.back_402d
            "403" -> imageId = R.mipmap.back_403d
            "404" -> imageId = R.mipmap.back_404d
            "405" -> imageId = R.mipmap.back_405d
            "406" -> imageId = R.mipmap.back_406d
            "407" -> imageId = R.mipmap.back_407d
            "408" -> imageId = R.mipmap.back_408d
            "409" -> imageId = R.mipmap.back_409d
            "410" -> imageId = R.mipmap.back_410d
            "499" -> imageId = R.mipmap.back_499d
            "500" -> imageId = R.mipmap.back_500d
            "501" -> imageId = R.mipmap.back_501d
            "502" -> imageId = R.mipmap.back_502d
            "503" -> imageId = R.mipmap.back_503d
            "504" -> imageId = R.mipmap.back_504d
            "507" -> imageId = R.mipmap.back_507d
            "508" -> imageId = R.mipmap.back_508d
            "509" -> imageId = R.mipmap.back_509d
            "510" -> imageId = R.mipmap.back_510d
            "511" -> imageId = R.mipmap.back_511d
            "512" -> imageId = R.mipmap.back_512d
            "513" -> imageId = R.mipmap.back_513d
            "514" -> imageId = R.mipmap.back_514d
            "515" -> imageId = R.mipmap.back_515d
            "900" -> imageId = R.mipmap.back_900d
            "901" -> imageId = R.mipmap.back_901d
            "999" -> imageId = R.mipmap.back_999d
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
            "100" -> imageId = R.mipmap.back_100n
            "101" -> imageId = R.mipmap.back_101n
            "102" -> imageId = R.mipmap.back_102n
            "103" -> imageId = R.mipmap.back_103n
            "104" -> imageId = R.mipmap.back_104n
            "200" -> imageId = R.mipmap.back_200n
            "201" -> imageId = R.mipmap.back_210n
            "202" -> imageId = R.mipmap.back_202n
            "203" -> imageId = R.mipmap.back_203n
            "204" -> imageId = R.mipmap.back_204n
            "205" -> imageId = R.mipmap.back_205n
            "206" -> imageId = R.mipmap.back_206n
            "207" -> imageId = R.mipmap.back_207n
            "208" -> imageId = R.mipmap.back_208n
            "209" -> imageId = R.mipmap.back_209n
            "210" -> imageId = R.mipmap.back_210n
            "211" -> imageId = R.mipmap.back_211n
            "212" -> imageId = R.mipmap.back_212n
            "213" -> imageId = R.mipmap.back_213n
            "300" -> imageId = R.mipmap.back_300n
            "301" -> imageId = R.mipmap.back_301n
            "302" -> imageId = R.mipmap.back_302n
            "303" -> imageId = R.mipmap.back_303n
            "304" -> imageId = R.mipmap.back_304n
            "305" -> imageId = R.mipmap.back_305n
            "306" -> imageId = R.mipmap.back_306n
            "307" -> imageId = R.mipmap.back_307n
            "308" -> imageId = R.mipmap.back_308n
            "309" -> imageId = R.mipmap.back_309n
            "310" -> imageId = R.mipmap.back_310n
            "311" -> imageId = R.mipmap.back_311n
            "312" -> imageId = R.mipmap.back_312n
            "313" -> imageId = R.mipmap.back_313n
            "314" -> imageId = R.mipmap.back_314n
            "315" -> imageId = R.mipmap.back_315n
            "316" -> imageId = R.mipmap.back_316n
            "317" -> imageId = R.mipmap.back_317n
            "318" -> imageId = R.mipmap.back_318n
            "399" -> imageId = R.mipmap.back_399n
            "400" -> imageId = R.mipmap.back_400n
            "401" -> imageId = R.mipmap.back_401n
            "402" -> imageId = R.mipmap.back_402n
            "403" -> imageId = R.mipmap.back_403n
            "404" -> imageId = R.mipmap.back_404n
            "405" -> imageId = R.mipmap.back_405n
            "406" -> imageId = R.mipmap.back_406n
            "407" -> imageId = R.mipmap.back_407n
            "408" -> imageId = R.mipmap.back_408n
            "409" -> imageId = R.mipmap.back_409n
            "410" -> imageId = R.mipmap.back_410n
            "499" -> imageId = R.mipmap.back_499n
            "500" -> imageId = R.mipmap.back_500n
            "501" -> imageId = R.mipmap.back_501n
            "502" -> imageId = R.mipmap.back_502n
            "503" -> imageId = R.mipmap.back_503n
            "504" -> imageId = R.mipmap.back_504n
            "507" -> imageId = R.mipmap.back_507n
            "508" -> imageId = R.mipmap.back_508n
            "509" -> imageId = R.mipmap.back_509n
            "510" -> imageId = R.mipmap.back_510n
            "511" -> imageId = R.mipmap.back_511n
            "512" -> imageId = R.mipmap.back_512n
            "513" -> imageId = R.mipmap.back_513n
            "514" -> imageId = R.mipmap.back_514n
            "515" -> imageId = R.mipmap.back_515n
            "900" -> imageId = R.mipmap.back_900n
            "901" -> imageId = R.mipmap.back_901n
            "999" -> imageId = R.mipmap.back_999n
            else -> imageId = R.mipmap.back_100n
        }
        return imageId
    }
}