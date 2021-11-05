package com.zj.weather.ui.view

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.MainViewModel
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlaySuccess
import com.zj.weather.common.lce.LcePage
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.*
import com.zj.weather.utils.IconUtils
import com.zj.weather.utils.ImageLoader
import java.lang.NullPointerException

@Composable
fun WeatherPage(
    mainViewModel: MainViewModel,
    cityInfo: CityInfo,
    onErrorClick: () -> Unit,
    cityList: () -> Unit,
    cityListClick: () -> Unit
) {
    val context = LocalContext.current
    val weatherModel by mainViewModel.weatherModel.observeAsState(PlayError(NullPointerException()))
//    val weatherNow by mainViewModel.weatherNow.observeAsState()
//    val todayBean by mainViewModel.todayBean.observeAsState()
//    val airNowBean by mainViewModel.airNowBean.observeAsState()
//    val hourlyBeanList by mainViewModel.hourlyBeanList.observeAsState(listOf())
//    val dayBeanList by mainViewModel.dayBeanList.observeAsState(listOf())
    val scrollState = rememberScrollState()
    val fontSize = (50f / (scrollState.value / 2) * 70).coerceAtLeast(20f).coerceAtMost(45f).sp
    val config = LocalConfiguration.current


    LcePage(playState = weatherModel, onErrorClick = onErrorClick) {
        val weather = (weatherModel as PlaySuccess).data
        Box(modifier = Modifier.fillMaxSize()) {
            ImageLoader(
                modifier = Modifier.fillMaxSize(),
                data = IconUtils.getWeatherBack(context, weather.nowBaseBean.icon)
            )
            val isLand = config.orientation == Configuration.ORIENTATION_LANDSCAPE
            if (isLand) {
                // 横屏适配
                Row(modifier = Modifier.fillMaxSize()) {
                    val landModifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                    Column(
                        modifier = landModifier,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        // 天气头部，向上滑动时会进行隐藏
                        HeaderWeather(
                            fontSize,
                            cityList,
                            cityListClick,
                            cityInfo,
                            weather.nowBaseBean,
                            true
                        )

                        // 天气动画
                        WeatherAnimation(weather.nowBaseBean.icon)
                    }
                    WeatherContent(
                        landModifier, scrollState, fontSize, cityList,
                        cityListClick, cityInfo, weather.nowBaseBean, weather.dailyBean,
                        weather.airNowBean, weather.hourlyBeanList, weather.dailyBeanList, true
                    )
                }
            } else {
                // 竖屏适配
                // 正常状态下是隐藏状态，向上滑动时展示
                ShrinkHeaderHeather(
                    fontSize, cityInfo, cityList, cityListClick, weather.nowBaseBean
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(if (fontSize.value > 40f) 0.dp else 110.dp))
                    WeatherContent(
                        Modifier, scrollState, fontSize, cityList, cityListClick,
                        cityInfo, weather.nowBaseBean, weather.dailyBean,
                        weather.airNowBean, weather.hourlyBeanList, weather.dailyBeanList
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherContent(
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