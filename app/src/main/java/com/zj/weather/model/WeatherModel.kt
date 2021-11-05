package com.zj.weather.model

import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean

/**
 * @param nowBaseBean 当前的天气
 * @param hourlyBeanList 未来24小时的天气预报
 * @param dailyBean 当天的天气预报
 * @param dailyBeanList 未来7天的天气预报
 * @param airNowBean 空气质量
 */
data class WeatherModel(
    val nowBaseBean: WeatherNowBean.NowBaseBean,
    val hourlyBeanList: List<WeatherHourlyBean.HourlyBean>,
    val dailyBean: WeatherDailyBean.DailyBean?,
    val dailyBeanList: List<WeatherDailyBean.DailyBean>,
    val airNowBean: AirNowBean.NowBean
)