package com.zj.weather

import android.app.Application
import android.location.Address
import android.location.Location
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
import com.qweather.sdk.view.QWeather.*
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.getDateWeekName
import com.zj.weather.utils.getDefaultLocale
import com.zj.weather.utils.getTimeName
import com.zj.weather.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/11/02
 * 描述：PlayAndroid
 *
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private var language: Lang = getDefaultLocale(getApplication())
    private val cityInfoDao = PlayWeatherDatabase.getDatabase(getApplication()).cityInfoDao()

    private val _weatherNow = MutableLiveData(WeatherNowBean.NowBaseBean())
    val weatherNow: LiveData<WeatherNowBean.NowBaseBean> = _weatherNow

    fun onWeatherNowChanged(weatherNowBean: WeatherNowBean.NowBaseBean) {
        if (_weatherNow.value == weatherNowBean) {
            return
        }
        _weatherNow.value = weatherNowBean
    }

    private val _hourlyBeanList = MutableLiveData(listOf<WeatherHourlyBean.HourlyBean>())
    val hourlyBeanList: LiveData<List<WeatherHourlyBean.HourlyBean>> = _hourlyBeanList

    fun onWeather24HourChanged(hourlyBean: List<WeatherHourlyBean.HourlyBean>) {
        if (_hourlyBeanList.value == hourlyBean) {
            return
        }
        _hourlyBeanList.value = hourlyBean
    }

    private val _dayBeanList = MutableLiveData(listOf<WeatherDailyBean.DailyBean>())
    val dayBeanList: LiveData<List<WeatherDailyBean.DailyBean>> = _dayBeanList

    fun onWeather7DayChanged(dailyBeanList: List<WeatherDailyBean.DailyBean>) {
        if (_dayBeanList.value == dailyBeanList) {
            return
        }
        _dayBeanList.value = dailyBeanList
    }

    private val _airNowBean = MutableLiveData(listOf<AirNowBean.AirNowStationBean>())
    val airNowBean: LiveData<List<AirNowBean.AirNowStationBean>> = _airNowBean

    fun onAirNowChanged(airNowList: List<AirNowBean.AirNowStationBean>) {
        if (_airNowBean.value == airNowList) {
            return
        }
        _airNowBean.value = airNowList
    }

    private val _locationBeanList = MutableLiveData(listOf<GeoBean.LocationBean>())
    val locationBeanList: LiveData<List<GeoBean.LocationBean>> = _locationBeanList

    fun onLocationBeanListChanged(hourlyBean: List<GeoBean.LocationBean>) {
        if (_locationBeanList.value == hourlyBean) {
            return
        }
        _locationBeanList.value = hourlyBean
    }

    private val _searchCityInfo = MutableLiveData(0)
    val searchCityInfo: LiveData<Int> = _searchCityInfo

    fun onSearchCityInfoChanged(page: Int) {
        if (_searchCityInfo.value == page) {
            return
        }
        _searchCityInfo.value = page
    }

    private val _cityInfoList = MutableLiveData(listOf<CityInfo>())
    val cityInfoList: LiveData<List<CityInfo>> = _cityInfoList

    private fun onCityInfoListChanged(list: List<CityInfo>) {
        if (_cityInfoList.value == list) {
            return
        }
        _cityInfoList.value = list
    }

    fun resetLanguage() {
        language = getDefaultLocale(getApplication())
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
     * lang     (选填)多语言，可以不使用该参数，默认为简体中文
     * unit     (选填)单位选择，公制（m）或英制（i），默认为公制单位
     * listener 网络访问结果回调
     */
    private fun getWeatherNow(location: String = "CN101010100") {
        Log.e(TAG, "getWeatherNow: 查询的城市:$location")
        getWeatherNow(getApplication(), location, language,
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

    /**
     * 当前的空气质量
     */
    private fun getAirNow(location: String = "CN101010100") {
        getAirNow(getApplication(), location, language, object : OnResultAirNowListener {
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
                                "${getApplication<Application>().getString(R.string.air_quality_warn)}${airNowStationBean.primary}"
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

    /**
     * 未来24小时每小时的天气预报
     */
    private fun getWeather24Hour(location: String = "CN101010100") {
        getWeather24Hourly(getApplication(), location, language,
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
                            hourlyBean.fxTime = getTimeName(getApplication(), hourlyBean.fxTime)
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

    /**
     * 未来7天天气预报
     */
    private fun getWeather7Day(location: String = "CN101010100") {
        getWeather7D(getApplication(), location, language,
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
                            dailyBean.fxDate = getDateWeekName(getApplication(), dailyBean.fxDate)
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
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        getGeoCityLookup(getApplication(), cityName, object : OnResultGeoListener {
            override fun onError(e: Throwable?) {
                onLocationBeanListChanged(listOf())
                Log.e(TAG, "getGeoCityLookup onError: ${e?.message}")
                showToast(getApplication(), R.string.add_location_warn2)
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
                    onLocationBeanListChanged(geoBean.locationBean)
                } else {
                    //在此查看返回数据失败的原因
                    val code: Code = geoBean.code
                    Log.i(TAG, "getGeoCityLookup failed code: $code")
                    showToast(getApplication(), code.txt)
                }
            }
        })
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        getGeoTopCity(getApplication(), 20, Range.CN, language, object : OnResultGeoListener {
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
                    onLocationBeanListChanged(geoBean.locationBean)
                } else {
                    //在此查看返回数据失败的原因
                    val code: Code = geoBean.code
                    Log.i(TAG, "getGeoTopCity failed code: $code")
                    showToast(getApplication(), code.txt)
                }
            }
        })
    }

    fun getCityList() {
        viewModelScope.launch(Dispatchers.IO) {
            var cityInfoList = cityInfoDao.getCityInfoList()
            if (cityInfoList.isNullOrEmpty()) {
                cityInfoList = listOf(
                    CityInfo(
                        location = "CN101010100",
                        name = getApplication<Application>().getString(R.string.default_location),
                    )
                )
            } else {
                Log.e(TAG, "NavGraph: cityInfoList:$cityInfoList")
            }
            withContext(Dispatchers.Main) {
                onCityInfoListChanged(cityInfoList)
            }
        }
    }

    fun getSyncCityList(): List<CityInfo> {
        var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
        if (cityInfoList.isNullOrEmpty()) {
            cityInfoList = listOf(
                CityInfo(
                    location = "CN101010100",
                    name = getApplication<Application>().getString(R.string.default_location)
                )
            )
        } else {
            Log.e(TAG, "NavGraph: cityInfoList:$cityInfoList")
        }
        return cityInfoList
    }

    fun deleteCityInfo(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            cityInfoDao.delete(cityInfo)
            getCityList()
        }
    }

    fun hasLocation(): Boolean {
        val isLocation = runBlocking { cityInfoDao.getIsLocationList() }
        return isLocation.isNotEmpty()
    }

    fun getCount(): Int {
        return runBlocking { cityInfoDao.getCount() }
    }

    fun insertCityInfo(cityInfo: CityInfo, onSuccessListener: () -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val hasLocation = cityInfoDao.getHasLocation(cityInfo.name)
            if (hasLocation.isNullOrEmpty()) {
                cityInfoDao.insert(cityInfo)
                withContext(Dispatchers.Main) {
                    onSuccessListener()
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToast(getApplication(), R.string.add_location_warn)
                }
            }
            getCityList()
        }
    }

    fun updateCityInfo(
        location: Location,
        result: MutableList<Address>
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val isLocationList = cityInfoDao.getIsLocationList()
            val cityInfo = CityInfo(
                location = "${location.longitude},${
                    location.latitude
                }",
                name = result[0].featureName ?: "",
                isLocation = 1,
                province = result[0].adminArea,
                city = result[0].locality
            )
            if (isLocationList.isNotEmpty()) {
                Log.d(TAG, "updateCityInfo: 数据库中没有当前的数据，需要新增")
                cityInfoDao.update(cityInfo)
            } else {
                cityInfoDao.insert(cityInfo)
                Log.d(TAG, "updateCityInfo: 数据库中已经存在当前的数据，需要修改")
            }
            getCityList()
            withContext(Dispatchers.Main) {
                onSearchCityInfoChanged(0)
            }
        }
    }

}