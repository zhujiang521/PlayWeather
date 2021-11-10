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
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.room.entity.CityInfo

@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    fontSize: TextUnit,
    cityList: () -> Unit,
    cityListClick: () -> Unit,
    cityInfo: CityInfo,
    weatherNow: WeatherNowBean.NowBaseBean?,
    dailyBean: WeatherDailyBean.DailyBean?,
    airNowBean: AirNowBean.NowBean?,
    hourlyBeanList: List<WeatherHourlyBean.HourlyBean>,
    dayBeanList: List<WeatherDailyBean.DailyBean>,
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
            HeaderWeather(fontSize, cityList, cityListClick, cityInfo, weatherNow)

            // 天气动画
            WeatherAnimation(weatherNow?.icon)

            Spacer(modifier = Modifier.height(10.dp))
        }

        // 当前空气质量
        AirQuality(airNowBean)

        Spacer(modifier = Modifier.height(10.dp))

        // 未来24小时天气预报
        HourWeather(hourlyBeanList)

        Spacer(modifier = Modifier.height(10.dp))

        // 未来7天天气预报
        DayWeather(dayBeanList)

        // 当天具体天气数值
        DayWeatherContent(weatherNow, dailyBean)
    }
}