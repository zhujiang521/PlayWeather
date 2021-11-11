package com.zj.weather.ui.view.weather.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import com.google.gson.Gson
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import com.zj.weather.R
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.*
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume

@ViewModelScoped
class WeatherRepository @Inject constructor(private val context: Application) {

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    /**
     * 获取现在的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     * @param lang 获取的语言
     */
    suspend fun getWeatherNow(location: String, lang: Lang) =
        suspendCancellableCoroutine<WeatherNowBean.NowBaseBean> { continuation ->
            QWeather.getWeatherNow(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherNowListener {
                    override fun onError(e: Throwable) {
                        XLog.e("getWeather24Hour onError: $e")
                        showToast(context, e.message)
                    }

                    override fun onSuccess(weatherNowBean: WeatherNowBean) {
                        XLog.d(
                            "getWeather onSuccess: " + Gson().toJson(weatherNowBean)
                        )
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === weatherNowBean.code) {
                            continuation.resume(weatherNowBean.now)
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code = weatherNowBean.code
                            XLog.w("failed code: $code")
                            showToast(context, code.txt)
                        }
                    }
                })
        }

    /**
     * 获取未来24小时的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     * @param lang 获取的语言
     */
    suspend fun getWeather24Hour(location: String, lang: Lang) =
        suspendCancellableCoroutine<List<WeatherHourlyBean.HourlyBean>> { continuation ->
            QWeather.getWeather24Hourly(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherHourlyListener {
                    override fun onError(e: Throwable) {
                        XLog.e("getWeather24Hour onError: $e")
                        showToast(context, e.message)
                    }

                    override fun onSuccess(weatherHourlyBean: WeatherHourlyBean?) {
                        XLog.d(
                            "getWeather24Hour onSuccess: " + Gson().toJson(weatherHourlyBean)
                        )
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === weatherHourlyBean?.code) {
                            weatherHourlyBean.hourly.forEach { hourlyBean ->
                                hourlyBean.fxTime = getTimeName(context, hourlyBean.fxTime)
                            }
                            continuation.resume(weatherHourlyBean.hourly)
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code? = weatherHourlyBean?.code
                            XLog.w("failed code: $code")
                        }
                    }
                })
        }

    /**
     * 获取未来7天的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     * @param lang 获取的语言
     */
    suspend fun getWeather7Day(location: String, lang: Lang) =
        suspendCancellableCoroutine<Pair<WeatherDailyBean.DailyBean?, List<WeatherDailyBean.DailyBean>>> { continuation ->
            QWeather.getWeather7D(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherDailyListener {
                    override fun onError(e: Throwable) {
                        XLog.e("getWeather24Hour onError: $e")
                        showToast(context, e.message)

                    }

                    override fun onSuccess(weatherDailyBean: WeatherDailyBean?) {
                        XLog.d(
                            "getWeather7Day onSuccess: " + Gson().toJson(weatherDailyBean)
                        )
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === weatherDailyBean?.code) {
                            val dailyBean = getTodayBean(weatherDailyBean.daily)
                            weatherDailyBean.daily.forEach { daily ->
                                daily.fxDate =
                                    getDateWeekName(context, daily.fxDate)
                            }
                            continuation.resume(Pair(dailyBean, weatherDailyBean.daily))
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code? = weatherDailyBean?.code
                            XLog.w("getWeather7Day failed code: $code")
                        }
                    }

                })
        }

    /**
     * 获取对应位置的空气质量
     *
     * @param location 位置 可能是地点、或者是地点id
     * @param lang 获取的语言
     */
    suspend fun getAirNow(location: String, lang: Lang) =
        suspendCancellableCoroutine<AirNowBean.NowBean> { continuation ->
            QWeather.getAirNow(context, location, lang,
                object : QWeather.OnResultAirNowListener {
                    override fun onError(e: Throwable) {
                        XLog.e("getWeather24Hour onError: $e")
                        showToast(context, e.message)
                    }

                    override fun onSuccess(airNowBean: AirNowBean?) {
                        XLog.d("getAirNow onSuccess: " + Gson().toJson(airNowBean))
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === airNowBean?.code) {
                            airNowBean.now.primary = if (airNowBean.now.primary == "NA") "" else {
                                "${context.getString(R.string.air_quality_warn)}${airNowBean.now.primary}"
                            }
                            continuation.resume(airNowBean.now)
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code? = airNowBean?.code
                            XLog.w("getAirNow failed code: $code")
                        }
                    }
                })
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
        onRefreshListener: () -> kotlin.Unit
    ) {
        if (result.isNullOrEmpty()) return
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
                cityInfoDao.update(cityInfo)
                withContext(Dispatchers.Main) {
                    onRefreshListener()
                }
            }
        } else {
            cityInfoDao.insert(cityInfo)
            withContext(Dispatchers.Main) {
                onRefreshListener()
            }
            XLog.e("updateCityInfo: 数据库中没有当前的数据，需要新增")
        }
    }

    private fun buildCityInfo(
        location: Location,
        address: Address
    ): CityInfo {
        return CityInfo(
            location = "${location.longitude},${
                location.latitude
            }",
            name = address.subLocality ?: "",
            isLocation = 1,
            province = address.adminArea,
            city = address.locality
        )
    }

    suspend fun refreshCityList(): List<CityInfo> {
        var cityInfoList = cityInfoDao.getCityInfoList()
        cityInfoList = makeDefault(context, cityInfoList)
        return cityInfoList
    }

    suspend fun updateCityIsIndex(
        cityInfoList: List<CityInfo>,
        onRefreshListener: (Int) -> kotlin.Unit
    ) {
        withContext(Dispatchers.IO) {
            cityInfoList.forEach {
                if (it.isIndex == 1) {
                    onRefreshListener(cityInfoList.indexOf(it))
                    it.isIndex = 0
                    cityInfoDao.update(it)
                }
            }
        }
    }


}