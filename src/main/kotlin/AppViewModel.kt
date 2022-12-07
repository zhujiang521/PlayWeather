import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.WeatherModel
import model.city.GeoBean
import model.weather.WeatherDailyBean
import network.PlayWeatherNetwork
import utils.DataStoreUtils

class AppViewModel {

    companion object {
        private const val CURRENT_CITY = "CURRENT_CITY"
        private const val DEFAULT_CITY = "北京"
        private const val CURRENT_CITY_ID = "CURRENT_CITY_ID"
        const val DEFAULT_CITY_ID = "CN101010100"
    }

    private val playWeatherNetwork = PlayWeatherNetwork

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

    val currentCity = DataStoreUtils.getData(CURRENT_CITY, DEFAULT_CITY)
    val currentCityId = DataStoreUtils.getData(CURRENT_CITY_ID, DEFAULT_CITY_ID)

    /**
     * place stone
     */
    suspend fun getWeather(location: String = DEFAULT_CITY_ID) {
        val weatherNow = playWeatherNetwork.getWeatherNow(location)
        // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
        // 目前发现在S版本上有问题，R中没有发现
        val weather24Hour = playWeatherNetwork.getWeather24Hour(location)
        val weather7Day = playWeatherNetwork.getWeather7Day(location)
        val airNow = playWeatherNetwork.getAirNowBean(location)
        val weatherLifeIndicesList = playWeatherNetwork.getWeatherLifeIndicesBean(location)

        val weatherModel = WeatherModel(
            nowBaseBean = weatherNow.now,
            hourlyBeanList = weather24Hour.hourly,
            dailyBean = if (weather7Day.daily.isNotEmpty()) weather7Day.daily[0] else WeatherDailyBean.DailyBean(),
            dailyBeanList = weather7Day.daily,
            airNowBean = airNow.now,
            weatherLifeList = weatherLifeIndicesList.daily ?: arrayListOf()
        )

        _weatherModel.value = weatherModel
    }

    private val _locationModel = MutableStateFlow(listOf<GeoBean.LocationBean>())
    val locationListData: Flow<List<GeoBean.LocationBean>>
        get() = _locationModel

    suspend fun searchCity(inputText: String) {
        val geoBean = playWeatherNetwork.getCityLookup(inputText)
        val locationBeanList = geoBean.location
        if (!locationBeanList.isNullOrEmpty()) {
            _locationModel.value = locationBeanList
        }
    }

    private val _topLocationModel = MutableStateFlow(listOf<GeoBean.LocationBean>())
    val topLocationListData: Flow<List<GeoBean.LocationBean>>
        get() = _topLocationModel

    suspend fun searchCity() {
        val geoBean = playWeatherNetwork.getCityTop()
        val locationBeanList = geoBean.topCityList
        if (!locationBeanList.isNullOrEmpty()) {
            _topLocationModel.value = locationBeanList
        }
    }

    fun clearSearchCity() {
        _locationModel.value = arrayListOf()
    }

}