package view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import model.WeatherModel

@Composable
fun RightInformation(
    modifier: Modifier,
    weatherModel: WeatherModel
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        // 当前空气质量
        AirQuality(weatherModel.airNowBean)

        // 未来24小时天气预报
        HourWeather(weatherModel.hourlyBeanList)

        // 未来7天天气预报
        WeekWeather(weatherModel.dailyBeanList)

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