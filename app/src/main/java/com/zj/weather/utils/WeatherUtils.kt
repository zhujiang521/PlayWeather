package com.zj.weather.utils

import android.content.Context
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import java.util.*

/**
 * 从7天中获取今天的天气，因为里面有紫外线和日出日落信息
 *
 * @param daily 7天的天气预报信息
 * @return 当天的天气预报
 */
fun getTodayBean(daily: MutableList<WeatherDailyBean.DailyBean>): WeatherDailyBean.DailyBean? {
    daily.forEach { dailyBean ->
        val date = dailyBean.fxDate
        val dateArray = date.split("-")
        val calendar = Calendar.getInstance()
        val todayWeek = calendar.get(Calendar.DAY_OF_WEEK)
        calendar.clear()
        calendar.set(dateArray[0].toInt(), dateArray[1].toInt() - 1, dateArray[2].toInt())
        val week = calendar.get(Calendar.DAY_OF_WEEK)
        if (todayWeek == week) {
            return dailyBean
        }
    }
    return null
}

/**
 * 获取紫外线指数对应的描述
 *
 * 指数值0到2，一般为阴或雨天，此时紫外线强度最弱，预报等级为一级；
 * 指数值3到4，一般为多云天气，此时紫外线强度较弱，预报等级为二级；
 * 指数值5到6，一般为少云天气，此时紫外线强度较强，预报等级为三级；
 * 指数值7到9，一般为晴天无云，此时紫外线强度很强，预报等级为四级；
 * 指数值达到或超过10，多为夏季晴日，紫外线强度特别强，预报等级为五级
 *
 * @param uv 紫外线指数
 * @return 对应的描述
 */
fun getUvIndexDesc(context: Context, uv: String?): String {
    XLog.d("getUvIndexDesc: $uv")
    if (uv == null || uv.isNullOrEmpty()) {
        return context.getString(R.string.uv_index1)
    }
    val uvIndex = uv.toInt()
    return if (uvIndex == 0 || uvIndex == 1 || uvIndex == 2) {
        context.getString(R.string.uv_index1)
    } else if (uvIndex == 3 || uvIndex == 4) {
        context.getString(R.string.uv_index2)
    } else if (uvIndex == 5 || uvIndex == 6) {
        context.getString(R.string.uv_index3)
    } else if (uvIndex == 7 || uvIndex == 8 || uvIndex == 9) {
        context.getString(R.string.uv_index4)
    } else {
        context.getString(R.string.uv_index5)
    }
}

/**
 * 获取当前该显示日出还是日落
 *
 * @param sunrise 日出时间
 * @param sunset 日落时间
 * @return 当前应该显示的值
 */
fun getSunriseSunsetContent(context: Context, sunrise: String, sunset: String): String {
    if (sunrise.isEmpty() || sunset.isEmpty()) {
        return "${context.getString(R.string.sun_sunrise)}7:00"
    }
    val sunrises = sunrise.split(":")
    val sunsets = sunset.split(":")
    XLog.d("getSunriseSunsetContent: sunrises:$sunrises  sunsets:$sunsets")
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return if (hour < sunrises[0].toInt()) {
        "${context.getString(R.string.sun_sunrise)}${sunrise}"
    } else {
        "${context.getString(R.string.sun_sunset)}${sunset}"
    }
}

/**
 * 创建默认的CityInfo
 */
fun makeDefault(context: Context, cityInfoList: List<CityInfo>?): List<CityInfo> {
    return if (cityInfoList.isNullOrEmpty()) {
        val cityInfo = listOf(
            CityInfo(
                location = "CN101010100",
                name = context.getString(R.string.default_location),
            )
        )
        cityInfo
    } else {
        XLog.e("cityInfoList:$cityInfoList")
        cityInfoList
    }
}