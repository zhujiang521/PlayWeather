import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.WeatherModel
import model.city.GeoBean
import model.weather.WeatherDailyBean
import model.weather.WeatherNowBean
import network.PlayWeatherNetwork
import utils.DataStoreUtils
import utils.getDateWeekName
import utils.lifePrefix

class AppViewModel {

    companion object {
        private const val CURRENT_CITY = "CURRENT_CITY"
        private const val DEFAULT_CITY = "北京"
        private const val CURRENT_CITY_ID = "CURRENT_CITY_ID"
        const val DEFAULT_CITY_ID = "CN101010100"
    }

    private val playWeatherNetwork = PlayWeatherNetwork

    /**
     * 当前天气
     */
    private val _weatherModel = MutableStateFlow(WeatherModel())
    val weatherModelData: Flow<WeatherModel>
        get() = _weatherModel

    suspend fun getWeather(locationModel: GeoBean.LocationBean) {
        val location = locationModel.id ?: DEFAULT_CITY_ID
        getWeather(location)
        DataStoreUtils.putData(
            CURRENT_CITY,
            if (location == DEFAULT_CITY_ID) DEFAULT_CITY else locationModel.name
        )
        DataStoreUtils.putData(CURRENT_CITY_ID, location)
    }

    /**
     * 当前城市及当前城市 ID
     */
    val currentCity = DataStoreUtils.getData(CURRENT_CITY, DEFAULT_CITY)
    val currentCityId = DataStoreUtils.getData(CURRENT_CITY_ID, DEFAULT_CITY_ID)

    /**
     * 获取天气信息
     *
     * @param location 位置
     */
    suspend fun getWeather(location: String = DEFAULT_CITY_ID) {
        val weatherNow = playWeatherNetwork.getWeatherNow(location)
        // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
        // 目前发现在S版本上有问题，R中没有发现
        val weather24Hour = playWeatherNetwork.getWeather24Hour(location)
        val weather7Day = playWeatherNetwork.getWeather7Day(location)
        val airNow = playWeatherNetwork.getAirNowBean(location)
        val weatherLifeIndicesList = playWeatherNetwork.getWeatherLifeIndicesBean(location)

        buildWeekWeather(weather7Day, weatherNow)

        weatherLifeIndicesList.daily?.apply {
            get(0).imgRes = "${lifePrefix}ic_life_sport.svg"
            get(1).imgRes = "${lifePrefix}ic_life_car.svg"
            get(2).imgRes = "${lifePrefix}ic_life_clothes.svg"
            get(3).imgRes = "${lifePrefix}ic_life_uv.svg"
            get(4).imgRes = "${lifePrefix}ic_life_travel.svg"
            get(5).imgRes = "${lifePrefix}ic_life_cold.svg"
        }

        val weatherModel = WeatherModel(
            nowBaseBean = weatherNow.now,
            hourlyBeanList = weather24Hour.hourly,
            dailyBean = if (weather7Day.daily.isNotEmpty()) weather7Day.daily[0] else WeatherDailyBean.DailyBean(),
            dailyBeanList = weather7Day.daily,
            airNowBean = airNow.now,
            weatherLifeList = weatherLifeIndicesList.daily ?: arrayListOf(),
            fxLink = weatherNow.fxLink
        )

        if (_weatherModel.value == weatherModel) {
            println("weatherModel same as before. Skip it")
            return
        }
        _weatherModel.value = weatherModel
    }

    /**
     * 为了构建7天天气的柱状图
     */
    private fun buildWeekWeather(
        weather7Day: WeatherDailyBean,
        weatherNow: WeatherNowBean
    ) {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        weather7Day.daily.forEach {
            val currentMin = it.tempMin?.toInt() ?: 0
            if (min > currentMin) {
                min = currentMin
            }

            val currentMax = it.tempMax?.toInt() ?: 0
            if (max < currentMax) {
                max = currentMax
            }
        }

        weather7Day.daily.forEach {
            it.weekMin = min
            it.weekMax = max

            it.fxDate = it.fxDate?.getDateWeekName() ?: "date"

            if (it.fxDate == "今天") {
                it.temp = weatherNow.now.temp?.toInt() ?: -100
            }
        }
    }

    /**
     * 位置列表
     */
    private val _locationModel = MutableStateFlow(listOf<GeoBean.LocationBean>())
    val locationListData: Flow<List<GeoBean.LocationBean>>
        get() = _locationModel

    suspend fun searchCity(inputText: String) {
        val geoBean = playWeatherNetwork.getCityLookup(inputText)
        val locationBeanList = geoBean.location
        if (!locationBeanList.isNullOrEmpty()) {
            if (_locationModel.value == locationBeanList) {
                println("locationModel same as before. Skip it")
            }
            _locationModel.value = locationBeanList
        }
    }

    /**
     * 热门城市列表
     */
    private val _topLocationModel = MutableStateFlow(listOf<GeoBean.LocationBean>())
    val topLocationListData: Flow<List<GeoBean.LocationBean>>
        get() = _topLocationModel

    suspend fun searchCity() {
        val geoBean = playWeatherNetwork.getCityTop()
        val locationBeanList = geoBean.topCityList
        if (!locationBeanList.isNullOrEmpty()) {
            if (_topLocationModel.value == locationBeanList) {
                println("topLocationModel same as before. Skip it")
            }
            _topLocationModel.value = locationBeanList
        }
    }

    /**
     * 清除城市搜索
     */
    fun clearSearchCity() {
        _locationModel.value = arrayListOf()
    }

}