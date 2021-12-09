package com.zj.weather.ui.view.weather

import android.content.res.Configuration
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.lce.LcePage
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.ui.view.weather.widget.HeaderWeather
import com.zj.weather.ui.view.weather.widget.ShrinkHeaderHeather
import com.zj.weather.ui.view.weather.widget.WeatherAnimation
import com.zj.weather.ui.view.weather.widget.WeatherContent
import com.zj.weather.utils.weather.IconUtils
import com.zj.weather.utils.ImageLoader

@Composable
fun WeatherPage(
    weatherViewModel: WeatherViewModel,
    cityInfo: CityInfo,
    onErrorClick: () -> Unit,
    cityList: () -> Unit,
    cityListClick: () -> Unit
) {
    val context = LocalContext.current
    val weatherModel by weatherViewModel.weatherModel.observeAsState(PlayLoading)
    val scrollState = rememberScrollState()
    val fontSize = (50f / (scrollState.value / 2) * 70).coerceAtLeast(20f).coerceAtMost(45f).sp
    val config = LocalConfiguration.current

    LcePage(playState = weatherModel, onErrorClick = onErrorClick) { weather ->
        Box(modifier = Modifier.fillMaxSize()) {
            ImageLoader(
                modifier = Modifier.fillMaxSize(),
                data = IconUtils.getWeatherBack(context, weather.nowBaseBean.icon)
            )
            val isLand = config.orientation == Configuration.ORIENTATION_LANDSCAPE
            if (isLand) {
                // 横屏适配
                HorizontalWeather(fontSize, cityList, cityListClick, cityInfo, weather, scrollState)
            } else {
                // 竖屏适配
                VerticalWeather(fontSize, cityInfo, cityList, cityListClick, weather, scrollState)
            }
        }
    }
}

@Composable
private fun VerticalWeather(
    fontSize: TextUnit,
    cityInfo: CityInfo,
    cityList: () -> Unit,
    cityListClick: () -> Unit,
    weather: WeatherModel,
    scrollState: ScrollState
) {
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
            Modifier, scrollState, fontSize, cityList, cityListClick, cityInfo, weather
        )
    }
}

@Composable
private fun HorizontalWeather(
    fontSize: TextUnit,
    cityList: () -> Unit,
    cityListClick: () -> Unit,
    cityInfo: CityInfo,
    weather: WeatherModel,
    scrollState: ScrollState
) {
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
                fontSize, cityList, cityListClick, cityInfo, weather.nowBaseBean, true
            )

            // 天气动画
            WeatherAnimation(weather.nowBaseBean.icon)
        }
        WeatherContent(
            landModifier, scrollState, fontSize, cityList,
            cityListClick, cityInfo, weather, true
        )
    }
}

@Preview(showBackground = false, name = "竖屏天气")
@Composable
fun VerticalWeatherPreview() {
    VerticalWeather(
        25.sp, CityInfo(name = "测试"), {},
        {}, buildWeatherModel(), rememberScrollState()
    )
}

@Composable
private fun buildWeatherModel(): WeatherModel {
    val nowBean = WeatherNowBean.NowBaseBean()
    nowBean.text = "多云"
    nowBean.temp = "25"
    return WeatherModel(
        nowBaseBean = nowBean,
        hourlyBeanList = listOf(),
        dailyBean = WeatherDailyBean.DailyBean(),
        dailyBeanList = listOf(),
        airNowBean = AirNowBean.NowBean()
    )
}

@Preview(showBackground = false, name = "横屏天气", heightDp = 320, widthDp = 640)
@Composable
fun HorizontalWeatherPreview() {
    HorizontalWeather(25.sp, {}, {}, CityInfo(name = "测试"),
        buildWeatherModel(), rememberScrollState()
    )
}