package com.zj.weather.view.weather

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.zj.model.PlayLoading
import com.zj.model.WeatherModel
import com.zj.model.air.AirNowBean
import com.zj.model.room.entity.CityInfo
import com.zj.model.weather.WeatherDailyBean
import com.zj.model.weather.WeatherNowBean
import com.zj.utils.lce.LcePage
import com.zj.utils.view.ImageLoader
import com.zj.utils.weather.IconUtils
import com.zj.weather.R
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.view.weather.widget.HeaderAction
import com.zj.weather.view.weather.widget.HeaderWeather
import com.zj.weather.view.weather.widget.WeatherAnimation
import com.zj.weather.view.weather.widget.WeatherContent

@Composable
fun WeatherPage(
    weatherViewModel: WeatherViewModel,
    cityInfo: CityInfo,
    onErrorClick: () -> Unit,
    cityList: () -> Unit,
    toCityMap: (Double, Double) -> Unit,
    cityListClick: () -> Unit
) {
    val context = LocalContext.current
    val weatherModel by weatherViewModel.weatherModel.observeAsState(PlayLoading)
    val config = LocalConfiguration.current

    LcePage(playState = weatherModel, onErrorClick = onErrorClick) { weather ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
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
                HorizontalWeather(cityInfo, weather, toCityMap)
            } else {
                // 竖屏适配
                VerticalWeather(cityInfo, weather, toCityMap)
            }
        }
    }
}

@Composable
private fun VerticalWeather(
    cityInfo: CityInfo,
    weather: WeatherModel,
    toCityMap: (Double, Double) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.page_margin))
    ) {
        WeatherContent(
            Modifier, cityInfo, weather, toCityMap = toCityMap
        )
    }
}

@Composable
private fun HorizontalWeather(
    cityInfo: CityInfo,
    weather: WeatherModel,
    toCityMap: (Double, Double) -> Unit,
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
                cityInfo, weather.nowBaseBean, true, toCityMap = toCityMap
            )

            // 天气动画
            WeatherAnimation(weather.nowBaseBean?.icon)
        }
        WeatherContent(
            landModifier, cityInfo,
            weather, true, toCityMap = toCityMap
        )
    }
}

@Preview(showBackground = false, name = "竖屏天气")
@Composable
fun VerticalWeatherPreview() {
    VerticalWeather(
        CityInfo(name = "测试"), buildWeatherModel(),
    ) { _, _ -> }
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
        CityInfo(name = "测试"), buildWeatherModel(),
    ) { _, _ -> }
}