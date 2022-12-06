package model.weather

import model.Refer
import model.weather.WeatherNowBean.NowBaseBean

class WeatherNowBean {
    val fxLink: String? = null
    val code: String? = null
    val refer: Refer? = null
    val now = NowBaseBean()
    val updateTime: String? = null

    class NowBaseBean {
        val vis: String? = null
        var temp: String? = null
        val obsTime: String? = null
        val icon: String? = null
        val wind360: String? = null
        val windDir: String? = null
        val pressure: String? = null
        val feelsLike: String? = null
        val cloud: String? = null
        val precip: String? = null
        val dew: String? = null
        val humidity: String? = null
        var text: String? = null
        val windSpeed: String? = null
        val windScale: String? = null
        var city: String? = null
    }
}