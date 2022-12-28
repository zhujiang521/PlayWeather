package com.zj.model.weather

import com.zj.model.Refer
import com.zj.model.weather.WeatherDailyBean.DailyBean
import java.util.ArrayList

data class WeatherDailyBean(
    val fxLink: String? = null,
    val code: String? = null,
    val refer: Refer? = null,
    val daily: List<DailyBean> = ArrayList(),
    val updateTime: String? = null
) {

    data class DailyBean(
        val moonset: String? = null,
        val windSpeedDay: String? = null,
        val sunrise: String? = null,
        val moonPhaseIcon: String? = null,
        val windScaleDay: String? = null,
        val windScaleNight: String? = null,
        val wind360Day: String? = null,
        var iconDay: String? = null,
        val wind360Night: String? = null,
        var tempMax: String? = null,
        val cloud: String? = null,
        val textDay: String? = null,
        val precip: String? = null,
        val textNight: String? = null,
        val humidity: String? = null,
        val moonPhase: String? = null,
        val windDirDay: String? = null,
        val windDirNight: String? = null,
        val vis: String? = null,
        var fxDate: String? = null,
        val moonrise: String? = null,
        val pressure: String? = null,
        val iconNight: String? = null,
        val sunset: String? = null,
        val windSpeedNight: String? = null,
        val uvIndex: String? = null,
        var tempMin: String? = null,
        var temp: Int = -100,
        var weekMin: Int = -20,
        var weekMax: Int = 40,
    )
}