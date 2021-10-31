package com.zj.weather

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
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
import com.qweather.sdk.view.QWeather.*
import com.zj.weather.utils.getDateWeekName
import com.zj.weather.utils.getTimeName
import com.zj.weather.utils.showToast


/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/5/17
 * 描述：PlayAndroid
 *
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val _weatherNow = MutableLiveData(WeatherNowBean.NowBaseBean())
    val weatherNow: LiveData<WeatherNowBean.NowBaseBean> = _weatherNow

    fun onWeatherNowChanged(weatherNowBean: WeatherNowBean.NowBaseBean) {
        _weatherNow.value = weatherNowBean
    }

    private val _hourlyBeanList = MutableLiveData(listOf<WeatherHourlyBean.HourlyBean>())
    val hourlyBeanList: LiveData<List<WeatherHourlyBean.HourlyBean>> = _hourlyBeanList

    fun onWeather24HourChanged(hourlyBean: List<WeatherHourlyBean.HourlyBean>) {
        _hourlyBeanList.value = hourlyBean
    }

    private val _dayBeanList = MutableLiveData(listOf<WeatherDailyBean.DailyBean>())
    val dayBeanList: LiveData<List<WeatherDailyBean.DailyBean>> = _dayBeanList

    fun onWeather7DayChanged(hourlyBean: List<WeatherDailyBean.DailyBean>) {
        _dayBeanList.value = hourlyBean
    }

    private val _airNowBean = MutableLiveData(listOf<AirNowBean.AirNowStationBean>())
    val airNowBean: LiveData<List<AirNowBean.AirNowStationBean>> = _airNowBean

    fun onAirNowChanged(hourlyBean: List<AirNowBean.AirNowStationBean>) {
        _airNowBean.value = hourlyBean
    }


    fun getWeather(location: String = "CN101010100") {
        getWeatherNow(location)
        getWeather24Hour(location)
        getWeather7Day(location)
        getAirNow(location)
    }

    /**
     * 实况天气数据
     * @param location 所查询的地区，可通过该地区名称、ID、IP和经纬度进行查询经纬度格式：经度,纬度
     *                 （英文,分隔，十进制格式，北纬东经为正，南纬西经为负)
     * @param lang     (选填)多语言，可以不使用该参数，默认为简体中文
     * @param unit     (选填)单位选择，公制（m）或英制（i），默认为公制单位
     * @param listener 网络访问结果回调
     */
    private fun getWeatherNow(location: String = "CN101010100") {
        getWeatherNow(getApplication(), location, Lang.ZH_HANS,
            Unit.METRIC, object : OnResultWeatherNowListener {
                override fun onError(e: Throwable) {
                    showToast(getApplication(), e.message)
                    Log.e(TAG, "getWeather onError: $e")
                }

                override fun onSuccess(weatherBean: WeatherNowBean) {
                    Log.i(TAG, "getWeather onSuccess: " + Gson().toJson(weatherBean))
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === weatherBean.code) {
                        val now = weatherBean.now
                        onWeatherNowChanged(now)
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code = weatherBean.code
                        Log.i(TAG, "failed code: $code")
                        showToast(getApplication(), code.txt)
                    }
                }
            })
    }

    private fun getAirNow(location: String = "CN101010100") {
        getAirNow(getApplication(), location, Lang.ZH_HANS,
            object : OnResultAirNowListener {
                override fun onError(e: Throwable) {
                    showToast(getApplication(), e.message)
                    Log.e(TAG, "getWeather24Hour onError: $e")
                }

                override fun onSuccess(airNowBean: AirNowBean?) {
                    Log.i(TAG, "getWeather24Hour onSuccess: " + Gson().toJson(airNowBean))
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === airNowBean?.code) {
                        airNowBean.airNowStationBean.forEach { airNowStationBean ->
                            airNowStationBean.primary =
                                if (airNowStationBean.primary == "NA") "" else {
                                    "，主要污染物为:${airNowStationBean.primary}"
                                }
                        }
                        onAirNowChanged(airNowBean.airNowStationBean)
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code? = airNowBean?.code
                        Log.i(TAG, "failed code: $code")
                    }
                }
            })
    }

    private fun getWeather24Hour(location: String = "CN101010100") {
        getWeather24Hourly(getApplication(), location, Lang.ZH_HANS,
            Unit.METRIC, object : OnResultWeatherHourlyListener {
                override fun onError(e: Throwable) {
                    showToast(getApplication(), e.message)
                    Log.e(TAG, "getWeather24Hour onError: $e")
                }

                override fun onSuccess(weatherBean: WeatherHourlyBean?) {
                    Log.i(TAG, "getWeather24Hour onSuccess: " + Gson().toJson(weatherBean))
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === weatherBean?.code) {
                        weatherBean.hourly.forEach { hourlyBean ->
                            hourlyBean.fxTime = getTimeName(hourlyBean.fxTime)
                        }
                        onWeather24HourChanged(weatherBean.hourly)
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code? = weatherBean?.code
                        Log.i(TAG, "failed code: $code")
                    }
                }
            })
    }

    private fun getWeather7Day(location: String = "CN101010100") {
        getWeather7D(getApplication(), location, Lang.ZH_HANS,
            Unit.METRIC, object : OnResultWeatherDailyListener {
                override fun onError(e: Throwable) {
                    showToast(getApplication(), e.message)
                    Log.e(TAG, "getWeather7Day onError: $e")
                }

                override fun onSuccess(weatherDailyBean: WeatherDailyBean?) {
                    Log.i(TAG, "getWeather7Day onSuccess: " + Gson().toJson(weatherDailyBean))
                    //先判断返回的status是否正确，当status正确时获取数据，若status不正确，可查看status对应的Code值找到原因
                    if (Code.OK === weatherDailyBean?.code) {
                        weatherDailyBean.daily.forEach { dailyBean ->
                            dailyBean.fxDate = getDateWeekName(dailyBean.fxDate)
                        }
                        onWeather7DayChanged(weatherDailyBean.daily)
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code? = weatherDailyBean?.code
                        Log.i(TAG, "getWeather7Day failed code: $code")
                    }
                }

            })
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        getGeoTopCity(getApplication(), 20, Range.CN, Lang.ZH_HANS,
            object : OnResultGeoListener {
                override fun onError(e: Throwable?) {
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
                        val locationBean = geoBean.locationBean
                        Log.e(TAG, "getGeoTopCity onSuccess: $locationBean")
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code = geoBean.code
                        Log.i(TAG, "getGeoTopCity failed code: $code")
                        showToast(getApplication(), code.txt)
                    }
                }
            })
    }

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        getGeoCityLookup(
            getApplication(),
            cityName,
            object : OnResultGeoListener {
                override fun onError(e: Throwable?) {
                    Log.e(TAG, "getGeoCityLookup onError: $e")
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
                        val locationBean = geoBean.locationBean
                        Log.e(TAG, "getGeoCityLookup onSuccess: $locationBean")
                    } else {
                        //在此查看返回数据失败的原因
                        val code: Code = geoBean.code
                        Log.i(TAG, "getGeoCityLookup failed code: $code")
                        showToast(getApplication(), code.txt)
                    }
                }
            })
    }

}