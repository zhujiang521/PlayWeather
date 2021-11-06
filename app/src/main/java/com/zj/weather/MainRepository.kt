package com.zj.weather

import android.app.Application
import android.location.Address
import android.location.Location
import android.util.Log
import com.google.gson.Gson
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Range
import com.qweather.sdk.bean.base.Unit
import com.qweather.sdk.bean.geo.GeoBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.qweather.sdk.view.QWeather
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.getDateWeekName
import com.zj.weather.utils.getTimeName
import com.zj.weather.utils.getTodayBean
import com.zj.weather.utils.showToast
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ActivityRetainedScoped
class MainRepository @Inject constructor(private val context: Application) {

    companion object {
        private const val TAG = "MainRepository"
    }

    private val cityInfoDao = PlayWeatherDatabase.getDatabase(context = context).cityInfoDao()

    /**
     * 获取现在的天气
     *
     * @param location 位置 可能是地点、或者是地点id
     * @param lang 获取的语言
     */
    suspend fun getWeatherNow(location: String = "CN101010100", lang: Lang) =
        suspendCancellableCoroutine<WeatherNowBean.NowBaseBean> { continuation ->
            QWeather.getWeatherNow(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherNowListener {
                    override fun onError(e: Throwable) {
                        Log.e(TAG, "getWeather24Hour onError: $e")
                        showToast(context, e.message)
                    }

                    override fun onSuccess(weatherNowBean: WeatherNowBean) {
                        Log.i(
                            TAG,
                            "getWeather onSuccess: " + Gson().toJson(weatherNowBean)
                        )
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === weatherNowBean.code) {
                            continuation.resume(weatherNowBean.now)
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code = weatherNowBean.code
                            Log.i(TAG, "failed code: $code")
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
    suspend fun getWeather24Hour(location: String = "CN101010100", lang: Lang) =
        suspendCancellableCoroutine<List<WeatherHourlyBean.HourlyBean>> { continuation ->
            QWeather.getWeather24Hourly(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherHourlyListener {
                    override fun onError(e: Throwable) {
                        Log.e(TAG, "getWeather24Hour onError: $e")
                        showToast(context, e.message)
                    }

                    override fun onSuccess(weatherHourlyBean: WeatherHourlyBean?) {
                        Log.i(
                            TAG,
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
                            Log.i(TAG, "failed code: $code")
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
    suspend fun getWeather7Day(location: String = "CN101010100", lang: Lang) =
        suspendCancellableCoroutine<Pair<WeatherDailyBean.DailyBean?, List<WeatherDailyBean.DailyBean>>> { continuation ->
            QWeather.getWeather7D(context, location, lang,
                Unit.METRIC, object : QWeather.OnResultWeatherDailyListener {
                    override fun onError(e: Throwable) {
                        Log.e(TAG, "getWeather24Hour onError: $e")
                        showToast(context, e.message)

                    }

                    override fun onSuccess(weatherDailyBean: WeatherDailyBean?) {
                        Log.i(
                            TAG,
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
                            Log.i(TAG, "getWeather7Day failed code: $code")
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
    suspend fun getAirNow(location: String = "CN101010100", lang: Lang) =
        suspendCancellableCoroutine<AirNowBean.NowBean> { continuation ->
            QWeather.getAirNow(context, location, lang,
                object : QWeather.OnResultAirNowListener {
                    override fun onError(e: Throwable) {
                        Log.e(TAG, "getWeather24Hour onError: $e")
                        showToast(context, e.message)

                    }

                    override fun onSuccess(airNowBean: AirNowBean?) {
                        Log.i(
                            TAG,
                            "getAirNow onSuccess: " + Gson().toJson(airNowBean)
                        )
                        //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === airNowBean?.code) {
                            airNowBean.now.primary = if (airNowBean.now.primary == "NA") "" else {
                                "${context.getString(R.string.air_quality_warn)}${airNowBean.now.primary}"
                            }
                            continuation.resume(airNowBean.now)
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code? = airNowBean?.code
                            Log.i(TAG, "getAirNow failed code: $code")
                        }
                    }
                })
        }

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    suspend fun getGeoCityLookup(cityName: String = "北京") =
        suspendCancellableCoroutine<PlayState<List<GeoBean.LocationBean>>> { continuation ->
            continuation.resume(PlayLoading)
            QWeather.getGeoCityLookup(context, cityName, object : QWeather.OnResultGeoListener {
                override fun onError(e: Throwable) {
                    continuation.resume(PlayError(e))
                    Log.e(TAG, "getGeoCityLookup onError: ${e.message}")
                    showToast(context, R.string.add_location_warn2)
                }

                override fun onSuccess(geoBean: GeoBean?) {
                    if (geoBean == null) {
                        Log.e(TAG, "getGeoCityLookup onError: 返回值为空")
                        return
                    }
                    val json = Gson().toJson(geoBean)
                    Log.i(TAG, "getGeoCityLookup onSuccess: $json")
                    // 先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === geoBean.code) {
                        continuation.resume(PlaySuccess(geoBean.locationBean))
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code = geoBean.code
                        Log.i(TAG, "getGeoCityLookup failed code: $code")
                        showToast(context = context, code.txt)
                    }
                }
            })
        }

    /**
     * 热门城市信息查询
     *
     * @param lang 获取的语言
     */
    suspend fun getGeoTopCity(lang: Lang) =
        suspendCancellableCoroutine<PlayState<List<GeoBean.LocationBean>>> { continuation ->
            continuation.resume(PlayLoading)
            QWeather.getGeoTopCity(context, 20, Range.CN, lang,
                object : QWeather.OnResultGeoListener {
                    override fun onError(e: Throwable) {
                        continuation.resume(PlayError(e))
                        Log.e(TAG, "getGeoTopCity onError: $e")
                    }

                    override fun onSuccess(geoBean: GeoBean?) {
                        if (geoBean == null) {
                            Log.e(TAG, "getGeoTopCity onError: 返回值为空")
                            return
                        }
                        val json = Gson().toJson(geoBean)
                        Log.i(TAG, "getGeoTopCity onSuccess: $json")
                        // 先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                        if (Code.OK === geoBean.code) {
                            continuation.resume(PlaySuccess(geoBean.locationBean))
                        } else {
                            //在此查看返回数据失败的原因
                            val code: Code = geoBean.code
                            Log.i(TAG, "getGeoTopCity failed code: $code")
                            continuation.resume(PlayError(NullPointerException(code.txt)))
                            showToast(context, code.txt)
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
    suspend fun updateCityInfo(location: Location, result: MutableList<Address>) {
        if (result.isNullOrEmpty()) return
        val address = result[0]
        val isLocationList = cityInfoDao.getIsLocationList()
        val cityInfo = CityInfo(
            location = "${location.longitude},${
                location.latitude
            }",
            name = address.subLocality ?: "",
            isLocation = 1,
            province = address.adminArea,
            city = address.locality
        )
        Log.d(TAG, "updateCityInfo: address:${address}")
        if (isLocationList.isNotEmpty()) {
            Log.d(TAG, "updateCityInfo: 数据库中已经存在当前的数据，需要修改")
            cityInfoDao.update(cityInfo)
        } else {
            cityInfoDao.insert(cityInfo)
            Log.d(TAG, "updateCityInfo: 数据库中没有当前的数据，需要新增")
        }
    }

}