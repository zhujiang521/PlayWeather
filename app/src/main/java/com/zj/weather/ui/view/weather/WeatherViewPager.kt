package com.zj.weather.ui.view.weather

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.utils.XLog
import com.zj.weather.utils.permission.FeatureThatRequiresLocationPermissions
import com.zj.weather.utils.weather.getCityIndex
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
) {
    val cityInfoList by weatherViewModel.cityInfoList.observeAsState()
    val pagerState = rememberPagerState()
    if (cityInfoList == null || cityInfoList.isNullOrEmpty()) {
        XLog.e("空的,刷新")
        if (pagerState.currentPage == 0) {
            FeatureThatRequiresLocationPermissions(weatherViewModel)
        }
    } else {
        val index =
            if (pagerState.currentPage > cityInfoList!!.size - 1) 0 else pagerState.currentPage
        val cityInfo = cityInfoList!![index]
        val location = getLocation(cityInfo = cityInfo)
        LaunchedEffect(location) {
            weatherViewModel.getWeather(location)
            XLog.e("查询 initialPage")
        }
        WeatherViewPager(
            weatherViewModel,
            cityInfoList!!, pagerState, getCityIndex(cityInfoList), toCityList, toWeatherList
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    cityInfoList: List<CityInfo>,
    pagerState: PagerState,
    initialPage: Int,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
) {
    XLog.e("initialPage:${initialPage}    currentPage:${pagerState.currentPage}")
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        if (initialPage >= 0 && initialPage < pagerState.pageCount) {
            coroutineScope.launch {
                pagerState.scrollToPage(initialPage)
            }
        }
        HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
            WeatherPage(
                weatherViewModel, cityInfoList[page],
                onErrorClick = {
                    val location = getLocation(cityInfoList[page])
                    weatherViewModel.getWeather(location)
                },
                cityList = toCityList, cityListClick = toWeatherList
            )
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

fun getLocation(
    cityInfo: CityInfo?
): String {
    if (cityInfo == null) return "CN101010100"
    return cityInfo.locationId.ifEmpty {
        cityInfo.location
    }
}
