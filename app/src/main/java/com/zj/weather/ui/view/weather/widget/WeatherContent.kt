package com.zj.weather.ui.view.weather.widget

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.entity.CityInfo

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    fontSize: TextUnit,
    cityList: () -> Unit,
    cityListClick: () -> Unit,
    cityInfo: CityInfo,
    weatherModel: WeatherModel?,
    isLand: Boolean = false
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        if (!isLand) {
            // 天气头部，向上滑动时会进行隐藏
            HeaderWeather(fontSize, cityList, cityListClick, cityInfo, weatherModel?.nowBaseBean)

            // 天气动画
            WeatherAnimation(weatherModel?.nowBaseBean?.icon)

            Spacer(modifier = Modifier.height(10.dp))
        }

        // 当前空气质量
        AirQuality(weatherModel?.airNowBean)

        // 未来24小时天气预报
        HourWeather(weatherModel?.hourlyBeanList)

        // 未来7天天气预报
        DayWeather(weatherModel?.dailyBeanList)

        // 当天具体天气数值
        DayWeatherContent(weatherModel)
    }
}