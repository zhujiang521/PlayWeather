package com.zj.weather.view.weather.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import com.zj.model.SUCCESSFUL
import com.zj.model.air.AirNowBean
import com.zj.model.getErrorText
import com.zj.model.indices.WeatherLifeIndicesBean
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.entity.CityInfo
import com.zj.model.weather.WeatherDailyBean
import com.zj.model.weather.WeatherHourlyBean
import com.zj.model.weather.WeatherNowBean
import com.zj.network.PlayWeatherNetwork
import com.zj.utils.XLog
import com.zj.utils.view.showToast
import com.zj.utils.weather.getDateWeekName
import com.zj.utils.weather.getTimeName
import com.zj.utils.weather.getTodayBean
import com.zj.weather.R
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlin.math.abs

@ViewModelScoped
class WeatherRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context).cityInfoDao()
    private val network = PlayWeatherNetwork(context.applicationContext)

    /**
     * 获取现在的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     */
    suspend fun getWeatherNow(location: String): WeatherNowBean.NowBaseBean? {
        val weatherNow = network.getWeatherNow(location)
        val code = weatherNow.code.toInt()
        return if (code == SUCCESSFUL) {
            weatherNow.now
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            null
        }
    }


    /**
     * 获取未来24小时的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     */
    suspend fun getWeather24Hour(location: String): List<WeatherHourlyBean.HourlyBean> {
        val weather24Hour = network.getWeather24Hour(location)
        val code = weather24Hour.code.toInt()
        return if (code == SUCCESSFUL) {
            weather24Hour.hourly.forEach { hourlyBean ->
                hourlyBean.fxTime = getTimeName(context, hourlyBean.fxTime)
            }
            weather24Hour.hourly
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            arrayListOf()
        }
    }


    /**
     * 获取未来7天的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     */
    suspend fun getWeather7Day(location: String): Pair<WeatherDailyBean.DailyBean?, List<WeatherDailyBean.DailyBean>?>? {
        val weather7Day = network.getWeather7Day(location)
        val code = weather7Day.code.toInt()
        return if (code == SUCCESSFUL) {
            val dailyBean = getTodayBean(weather7Day.daily)
            weather7Day.daily.forEach { daily ->
                daily.fxDate =
                    getDateWeekName(context, daily.fxDate)
            }
            Pair(dailyBean, weather7Day.daily)
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            null
        }
    }

    /**
     * 获取对应位置的空气质量
     *
     * @param location 位置 可能是地点、或者是地点id
     */
    suspend fun getAirNow(location: String): AirNowBean.NowBean? {
        val airNowBean = network.getAirNowBean(location)
        val code = airNowBean.code.toInt()
        return if (code == SUCCESSFUL) {
            airNowBean.now.primary = if (airNowBean.now.primary == "NA") "" else {
                "${context.getString(R.string.air_quality_warn)}${airNowBean.now.primary}"
            }
            airNowBean.now
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            null
        }
    }


    /**
     * 获取对应位置的生活指数
     *
     * @param location 位置 可能是地点、或者是地点id
     */
    suspend fun getWeatherLifeIndicesList(location: String): List<WeatherLifeIndicesBean.WeatherLifeIndicesItem> {
        val weatherLifeIndicesBean = network.getWeatherLifeIndicesBean(location)
        val code = weatherLifeIndicesBean.code.toInt()
        return if (code == SUCCESSFUL) {
            weatherLifeIndicesBean.daily
        } else {
            val text = getErrorText(code)
            showToast(context, text)
            XLog.e("code:$code, text:$text")
            arrayListOf()
        }
    }

    /**
     * 修改当前的位置信息
     *
     * @param location 位置
     * @param result Address
     */
    suspend fun updateCityInfo(
        location: Location,
        result: MutableList<Address>,
    ) {
        if (result.isEmpty()) return
        val address = result[0]
        val isLocationList = cityInfoDao.getIsLocationList()
        val cityInfo = buildCityInfo(location, address)
        XLog.e("updateCityInfo: address:${address}")
        if (isLocationList.isNotEmpty()) {
            cityInfo.uid = isLocationList[0].uid
            if (cityInfo == isLocationList[0]) {
                XLog.e("updateCityInfo: 数据库中已经存在当前的数据并且相等，无需修改:${cityInfo.uid}  ")
            } else {
                XLog.e("updateCityInfo: 数据库中已经存在当前的数据，需要修改:${cityInfo.uid}")
                updateCityIndex(cityInfo)
                cityInfoDao.update(cityInfo)
            }
        } else {
            updateCityIndex(cityInfo)
            cityInfoDao.insert(cityInfo)
            XLog.e("updateCityInfo: 数据库中没有当前的数据，需要新增")
        }
    }

    private suspend fun updateCityIndex(cityInfo: CityInfo) {
        val indexList = cityInfoDao.getIndexCity()
        if (indexList.isNotEmpty()) {
            val indexCity = indexList[0]
            indexCity.isIndex = 0
            cityInfoDao.update(indexCity)
        }
        cityInfo.isIndex = 1
    }

    private fun buildCityInfo(
        location: Location,
        address: Address
    ): CityInfo {
        return CityInfo(
            location = "${abs(location.longitude)},${
                abs(location.latitude)
            }",
            name = address.subLocality ?: "",
            isLocation = 1,
            province = address.adminArea,
            city = address.locality
        )
    }

    fun refreshCityList() = cityInfoDao.getCityInfoList()

}