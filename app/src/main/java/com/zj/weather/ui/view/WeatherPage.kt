package com.zj.weather.ui.view

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.MainViewModel
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.*
import com.zj.weather.utils.IconUtils
import com.zj.weather.utils.ImageLoader

@Composable
fun WeatherPage(
    mainViewModel: MainViewModel,
    cityInfo: CityInfo, cityListClick: () -> Unit
) {
    val context = LocalContext.current
    val weatherNow by mainViewModel.weatherNow.observeAsState()
    val airNowBean by mainViewModel.airNowBean.observeAsState(listOf())
    val hourlyBeanList by mainViewModel.hourlyBeanList.observeAsState(listOf())
    val dayBeanList by mainViewModel.dayBeanList.observeAsState(listOf())
    val scrollState = rememberScrollState()
    val fontSize = (50f / (scrollState.value / 2) * 70).coerceAtLeast(20f).coerceAtMost(45f).sp
    Box(modifier = Modifier.fillMaxSize()) {
        ImageLoader(
            modifier = Modifier.fillMaxSize(),
            data = IconUtils.getWeatherBack(context, weatherNow?.icon)
        )

        // 正常状态下是收缩状态，向上滑动时展示
        ShrinkHeaderHeather(fontSize, cityInfo, cityListClick, weatherNow)

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(if (fontSize.value > 40f) 0.dp else 110.dp))
            WeatherContent(
                scrollState,
                fontSize,
                cityListClick,
                cityInfo,
                weatherNow,
                airNowBean,
                hourlyBeanList,
                dayBeanList
            )
        }

    }
}

@Composable
private fun WeatherContent(
    scrollState: ScrollState,
    fontSize: TextUnit,
    cityListClick: () -> Unit,
    cityInfo: CityInfo,
    weatherNow: WeatherNowBean.NowBaseBean?,
    airNowBean: List<AirNowBean.AirNowStationBean>,
    hourlyBeanList: List<WeatherHourlyBean.HourlyBean>,
    dayBeanList: List<WeatherDailyBean.DailyBean>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        // 天气头部，向上滑动时会进行隐藏
        HeaderWeather(fontSize, cityListClick, cityInfo, weatherNow)

        // 天气动画
        WeatherAnimation(weatherNow?.icon)

        Spacer(modifier = Modifier.height(10.dp))

        // 当前空气质量
        AirQuality(airNowBean)

        Spacer(modifier = Modifier.height(10.dp))

        // 未来24小时天气预报
        HourWeather(hourlyBeanList)

        Spacer(modifier = Modifier.height(10.dp))

        // 未来7天天气预报
        DayWeather(dayBeanList)

        // 当天具体天气数值
        DayWeatherContent(weatherNow)
    }
}