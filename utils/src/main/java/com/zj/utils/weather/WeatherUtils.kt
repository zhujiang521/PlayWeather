package com.zj.utils.weather

import android.content.Context
import com.zj.model.room.entity.CityInfo
import com.zj.model.weather.WeatherDailyBean
import com.zj.utils.R
import com.zj.utils.XLog
import java.util.*

/**
 * 从7天中获取今天的天气，因为里面有紫外线和日出日落信息
 *
 * @param daily 7天的天气预报信息
 * @return 当天的天气预报
 */
fun getTodayBean(daily: List<WeatherDailyBean.DailyBean>): WeatherDailyBean.DailyBean? {
    daily.forEach { dailyBean ->
        val date = dailyBean.fxDate ?: return@forEach
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
    if (uv == null || uv.isEmpty()) {
        return context.getString(R.string.uv_index1)
    }
    return when (uv.toInt()) {
        0, 1, 2 -> {
            context.getString(R.string.uv_index1)
        }
        3, 4 -> {
            context.getString(R.string.uv_index2)
        }
        5, 6 -> {
            context.getString(R.string.uv_index3)
        }
        7, 8, 9 -> {
            context.getString(R.string.uv_index4)
        }
        else -> {
            context.getString(R.string.uv_index5)
        }
    }
}

fun getCityIndex(cityInfoList: List<CityInfo>?): Int {
    if (cityInfoList == null) return 0
    var city: CityInfo? = null
    for (index in cityInfoList.indices) {
        if (cityInfoList[index].isIndex == 1) {
            city = cityInfoList[index]
        }
    }
    return cityInfoList.indexOf(city)
}