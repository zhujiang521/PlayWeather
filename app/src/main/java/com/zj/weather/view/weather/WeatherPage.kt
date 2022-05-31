package com.zj.weather.view.weather

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.air.AirNowBean
import com.zj.model.weather.WeatherDailyBean
import com.zj.model.weather.WeatherNowBean
import com.zj.model.PlayLoading
import com.zj.utils.lce.LcePage
import com.zj.model.WeatherModel
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.view.weather.widget.HeaderAction
import com.zj.weather.view.weather.widget.HeaderWeather
import com.zj.weather.view.weather.widget.WeatherAnimation
import com.zj.weather.view.weather.widget.WeatherContent
import com.zj.utils.view.ImageLoader
import com.zj.utils.weather.IconUtils
import com.zj.weather.R

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
    val topPaddingSize = (50f / (scrollState.value / 2) * 70).coerceAtLeast(0f).coerceAtMost(45f).dp
    val config = LocalConfiguration.current

    LcePage(playState = weatherModel, onErrorClick = onErrorClick) { weather ->
        Box(modifier = Modifier.fillMaxSize()) {
            ImageLoader(
                modifier = Modifier.fillMaxSize(),
                data = IconUtils.getWeatherBack(context, weather.nowBaseBean?.icon)
            )
            val isLand = config.orientation == Configuration.ORIENTATION_LANDSCAPE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HeaderAction(
                    modifier = Modifier.weight(1f),
                    cityListClick = cityListClick,
                    cityList = cityList
                )
                if (isLand) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            if (isLand) {
                // 横屏适配
                HorizontalWeather(fontSize, cityInfo, weather, scrollState)
            } else {
                // 竖屏适配
                VerticalWeather(fontSize, topPaddingSize, cityInfo, weather, scrollState)
            }
        }
    }
}

@Composable
private fun VerticalWeather(
    fontSize: TextUnit,
    topPadding: Dp,
    cityInfo: CityInfo,
    weather: WeatherModel,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.page_margin))
    ) {
        WeatherContent(
            Modifier, scrollState, fontSize, topPadding, cityInfo, weather
        )
    }
}

@Composable
private fun HorizontalWeather(
    fontSize: TextUnit,
    cityInfo: CityInfo,
    weather: WeatherModel,
    scrollState: ScrollState
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.page_margin))
    ) {
        val landModifier = Modifier
            .weight(1f)
            .fillMaxHeight()
        Column(
            modifier = landModifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            // 天气头部
            HeaderWeather(
                fontSize, 0.dp, cityInfo, weather.nowBaseBean, true
            )

            // 天气动画
            WeatherAnimation(weather.nowBaseBean?.icon)
        }
        WeatherContent(
            landModifier, scrollState, fontSize, 0.dp, cityInfo,
            weather, true
        )
    }
}

@Preview(showBackground = false, name = "竖屏天气")
@Composable
fun VerticalWeatherPreview() {
    VerticalWeather(
        25.sp, 0.dp, CityInfo(name = "测试"), buildWeatherModel(),
        rememberScrollState()
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
    HorizontalWeather(
        25.sp, CityInfo(name = "测试"), buildWeatherModel(), rememberScrollState()
    )
}