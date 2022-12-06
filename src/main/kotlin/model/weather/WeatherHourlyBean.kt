package model.weather

import model.Refer
import java.util.ArrayList

class WeatherHourlyBean {
    val fxLink: String? = null
    val code: String? = null
    val refer: Refer? = null
    val updateTime: String? = null
    val hourly: List<HourlyBean> = ArrayList()

    class HourlyBean {
        var temp: String? = null
        var icon: String? = null
        val wind360: String? = null
        val windDir: String? = null
        val pressure: String? = null
        var fxTime: String? = null
        val pop: String? = null
        val cloud: String? = null
        val precip: String? = null
        val dew: String? = null
        val humidity: String? = null
        val text: String? = null
        val windSpeed: String? = null
        val windScale: String? = null
    }
}