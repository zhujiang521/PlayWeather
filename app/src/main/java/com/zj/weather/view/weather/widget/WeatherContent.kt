package com.zj.weather.view.weather.widget

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.zj.model.WeatherModel
import com.zj.model.room.entity.CityInfo

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    fontSize: TextUnit,
    topPadding: Dp,
    cityInfo: CityInfo,
    weatherModel: WeatherModel?,
    isLand: Boolean = false
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!isLand) {
            // 天气头部
            HeaderWeather(
                fontSize, topPadding, cityInfo, weatherModel?.nowBaseBean, false
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            if (!isLand) {
                // 天气动画
                WeatherAnimation(weatherModel?.nowBaseBean?.icon)
            }

            Spacer(modifier = Modifier.height(10.dp))

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
}