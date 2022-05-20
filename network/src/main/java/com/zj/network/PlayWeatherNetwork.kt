package com.zj.network

import android.content.Context
import com.zj.model.Lang
import com.zj.model.air.AirNowBean
import com.zj.model.city.GeoBean
import com.zj.model.city.TopGeoBean
import com.zj.model.indices.WeatherLifeIndicesBean
import com.zj.model.weather.WeatherDailyBean
import com.zj.model.weather.WeatherHourlyBean
import com.zj.model.weather.WeatherNowBean
import com.zj.network.service.AirNowService
import com.zj.network.service.CityLookupService
import com.zj.network.service.CityWeatherService
import com.zj.network.service.WeatherLifeIndicesService
import com.zj.utils.XLog
import com.zj.utils.getDefaultLocale

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/4/30
 * 描述：SunnyWeather
 *
 */
class PlayWeatherNetwork(private val context: Context) {

    private var language: Lang = context.getDefaultLocale()

    private val airNowService = ServiceCreator.create(AirNowService::class.java)

    suspend fun getWeatherLifeIndicesBean(location: String): WeatherLifeIndicesBean =
        airNowService.getWeatherLifeIndicesBean(location = location, lang = language.code)


    private val cityLookupService = ServiceCreator.createCity(CityLookupService::class.java)

    suspend fun getCityLookup(location: String): GeoBean {
        XLog.w(context.packageName)
        return cityLookupService.getCityLookup(location = location, lang = language.code)
    }

    suspend fun getCityTop(): TopGeoBean {
        XLog.w(context.packageName)
        return cityLookupService.getCityTop(lang = language.code)
    }


    private val cityWeatherService = ServiceCreator.create(CityWeatherService::class.java)

    suspend fun getWeatherNow(location: String): WeatherNowBean =
        cityWeatherService.getWeatherNow(location = location, lang = language.code)

    suspend fun getWeather24Hour(location: String): WeatherHourlyBean =
        cityWeatherService.getWeather24Hour(location = location, lang = language.code)

    suspend fun getWeather3Day(location: String): WeatherDailyBean =
        cityWeatherService.getWeather3Day(location = location, lang = language.code)

    suspend fun getWeather7Day(location: String): WeatherDailyBean =
        cityWeatherService.getWeather7Day(location = location, lang = language.code)

    private val weatherLifeIndicesService =
        ServiceCreator.create(WeatherLifeIndicesService::class.java)

    suspend fun getAirNowBean(location: String): AirNowBean =
        weatherLifeIndicesService.getAirNowBean(location = location, lang = language.code)

}