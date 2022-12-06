import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.WeatherModel
import view.*

@Composable
fun WeatherPage(appViewModel: AppViewModel) {
    LaunchedEffect(1) {
        appViewModel.getWeather()
    }
    val weatherModel by appViewModel.weatherModelData.collectAsState(WeatherModel())

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp).verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(painter = buildPainter("ic_launcher.png"), "")


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

        // 数据源
        Text(
            text = "数据来自和风天气",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 15.dp)
        )


    }

}