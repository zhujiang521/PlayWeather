package com.zj.weather.ui.view.weather

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.list.widget.DrawIndicator
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.utils.XLog
import com.zj.weather.utils.permission.FeatureThatRequiresLocationPermissions
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
) {
    val cityInfoList by weatherViewModel.cityInfoList.observeAsState(listOf())
    val initialPage by weatherViewModel.searchCityInfo.observeAsState(0)
    val pagerState = rememberPagerState()
    cityInfoList.apply {
        if (isNullOrEmpty()) return@apply
        val index = if (pagerState.currentPage > size - 1) {
            0
        } else pagerState.currentPage
        val cityInfo = get(index)
        val location = getLocation(cityInfo = cityInfo)
        if (location != null) {
            LaunchedEffect(location) {
                weatherViewModel.getWeather(location)
            }
            XLog.e("查询 initialPage:$initialPage")
        }
    }
    WeatherViewPager(
        weatherViewModel, initialPage,
        cityInfoList, pagerState, toCityList, toWeatherList
    )
}

@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    initialPage: Int = 0,
    cityInfoList: List<CityInfo>,
    pagerState: PagerState,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
) {
    if (pagerState.currentPage == 0) {
        FeatureThatRequiresLocationPermissions(weatherViewModel)
    }
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        if (initialPage > 0 && initialPage < pagerState.pageCount) {
            coroutineScope.launch {
                pagerState.scrollToPage(initialPage)
                weatherViewModel.onSearchCityInfoChanged(0)
            }
        }
        HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
            WeatherPage(
                weatherViewModel, cityInfoList[page],
                onErrorClick = {
                    val location = getLocation(cityInfoList[page])
                    if (location != null) {
                        weatherViewModel.getWeather(location)
                    }
                },
                cityList = toCityList, cityListClick = toWeatherList
            )
        }
    }
    DrawIndicator(
        pagerState = pagerState,
        hasCurrentPosition = weatherViewModel.hasLocation()
    )
}

fun getLocation(
    cityInfo: CityInfo?
): String? {
    if (cityInfo == null) return "CN101010100"
    return if (cityInfo.locationId.isNotEmpty()) {
        cityInfo.locationId
    } else {
        cityInfo.location
    }
}
