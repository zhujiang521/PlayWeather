import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import model.WeatherModel
import network.PlayWeatherNetwork

class AppViewModel {

    private val _weatherModel = MutableStateFlow(WeatherModel())
    val weatherModelData: Flow<WeatherModel>
        get() = _weatherModel

    /**
     * place stone
     */
    suspend fun getWeather(location: String = "CN101010100") {
        val weatherNow = PlayWeatherNetwork.getWeatherNow(location)
        // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
        // 目前发现在S版本上有问题，R中没有发现
        val weather24Hour = PlayWeatherNetwork.getWeather24Hour(location)
        val weather7Day = PlayWeatherNetwork.getWeather7Day(location)
        val airNow = PlayWeatherNetwork.getAirNowBean(location)
        val weatherLifeIndicesList = PlayWeatherNetwork.getWeatherLifeIndicesBean(location)

        val weatherModel = WeatherModel(
            nowBaseBean = weatherNow.now,
            hourlyBeanList = weather24Hour.hourly,
            dailyBean = weather7Day.daily[0],
            dailyBeanList = weather7Day.daily,
            airNowBean = airNow.now,
            weatherLifeList = weatherLifeIndicesList.daily ?: arrayListOf()
        )

        _weatherModel.value = weatherModel
    }

}