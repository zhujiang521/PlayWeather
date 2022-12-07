import AppViewModel.Companion.DEFAULT_CITY_ID
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import model.WeatherModel
import view.*

@Composable
fun WeatherPage(appViewModel: AppViewModel) {
    val weatherModel by appViewModel.weatherModelData.collectAsState(WeatherModel())
    val currentCityId by appViewModel.currentCityId.collectAsState(DEFAULT_CITY_ID)
    LaunchedEffect(currentCityId) {
        appViewModel.getWeather(currentCityId)
    }
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxSize().padding(10.dp),
    ) {
        // 信息区域（左边）
        Information(appViewModel, weatherModel.nowBaseBean, currentCityId)

        // 天气展示区域（右边）
        Column(
            modifier = Modifier.weight(1f).width(500.dp).verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // 当前空气质量
            AirQuality(weatherModel.airNowBean)

            // 未来24小时天气预报
            HourWeather(weatherModel.hourlyBeanList)

            // 未来7天天气预报
            DayWeather(weatherModel.dailyBeanList)

            // 当天具体天气数值
            DayWeatherContent(weatherModel)

            // 日出日落
            SunriseSunsetContent(weatherModel.dailyBean)

            // 当天生活指数
            LifeWeatherContent(weatherModel.weatherLifeList)

            // 数据来源
            SourceData()

        }
    }

}

