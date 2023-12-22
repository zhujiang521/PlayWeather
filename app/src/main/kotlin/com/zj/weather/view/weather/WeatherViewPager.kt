@file:OptIn(ExperimentalFoundationApi::class)

package com.zj.weather.view.weather

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zj.banner.utils.HorizontalPagerIndicator
import com.zj.model.room.entity.CityInfo
import com.zj.utils.XLog
import com.zj.utils.defaultCityState
import com.zj.utils.lce.NoContent
import com.zj.weather.permission.FeatureThatRequiresLocationPermissions
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.view.weather.widget.HeaderAction

@ExperimentalPermissionsApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toCityMap: (Double, Double) -> Unit,
    toWeatherList: () -> Unit
) {
    val cityInfoList by weatherViewModel.cityInfoList.collectAsState(initial = arrayListOf())
    Log.i("ZHUJIANG123", "Weather11111: ${cityInfoList.size}")
    if (cityInfoList.isEmpty()) {
        XLog.w("Empty, refresh")
        FeatureThatRequiresLocationPermissions(weatherViewModel)
        NoCityContent(toWeatherList, toCityList)
    } else {
        Weather(cityInfoList, weatherViewModel, toCityList, toCityMap, toWeatherList)
    }
}

@ExperimentalPermissionsApi
@Composable
private fun Weather(
    cityInfoList: List<CityInfo>,
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toCityMap: (Double, Double) -> Unit,
    toWeatherList: () -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = 0, initialPageOffsetFraction = 0f
    ) {
        cityInfoList.size
    }
    CurrentPageEffect(pagerState, cityInfoList, weatherViewModel)
    val value = defaultCityState.value
    val indexOf = cityInfoList.indexOf(value)
    Log.i("ZHUJIANG123", "Weather22222: $value  $indexOf   ${ pagerState.currentPage}")
    if (indexOf > 0 && indexOf != pagerState.currentPage) {
        LaunchedEffect(Unit) {
            pagerState.animateScrollToPage(indexOf)
            Log.i("ZHUJIANG123", "Weather: $indexOf")
        }
    }
    WeatherViewPager(
        weatherViewModel,
        cityInfoList,
        pagerState,
        toCityList,
        toCityMap,
        toWeatherList
    )
}

@Composable
fun CurrentPageEffect(
    pagerState: PagerState, cityInfoList: List<CityInfo>, weatherViewModel: WeatherViewModel
) {
    if (pagerState.isScrollInProgress) {
        return
    }
    LaunchedEffect(pagerState.currentPage) {
        val index =
            if (pagerState.currentPage > cityInfoList.size - 1) 0 else pagerState.currentPage
        val cityInfo = cityInfoList[index]
        weatherViewModel.getWeather(cityInfo)
        val info = defaultCityState.value
        if (info.location.isEmpty() && info.locationId.isEmpty()) {
            defaultCityState.value = cityInfo
        }
        XLog.i("Query initialPage")
    }
}

@Composable
private fun NoCityContent(
    toWeatherList: () -> Unit,
    toCityList: () -> Unit,
) {
    val config = LocalConfiguration.current
    val isLand = config.orientation == Configuration.ORIENTATION_LANDSCAPE
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderAction(
                modifier = Modifier.weight(1f), cityListClick = toWeatherList, cityList = toCityList
            )
            if (isLand) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        NoContent()
    }
}

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalPermissionsApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    cityInfoList: List<CityInfo>,
    pagerState: PagerState,
    toCityList: () -> Unit,
    toCityMap: (Double, Double) -> Unit,
    toWeatherList: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        HorizontalPager(modifier = Modifier, state = pagerState, key = {
            try {
                cityInfoList[it].locationId
            } catch (e: Exception) {
                e.printStackTrace()
                System.currentTimeMillis()
            }
        }, pageContent = { page ->
            val isRefreshing by weatherViewModel.isRefreshing.collectAsState()

            val pullRefreshState = rememberPullRefreshState(
                isRefreshing,
                { weatherViewModel.refresh(cityInfoList[page]) })

            Box(
                Modifier.pullRefresh(pullRefreshState)
            ) {
                WeatherPage(
                    weatherViewModel, cityInfoList[page], onErrorClick = {
                        weatherViewModel.getWeather(cityInfoList[page])
                    }, cityList = toCityList, toCityMap = toCityMap, cityListClick = toWeatherList
                )

                PullRefreshIndicator(
                    isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter)
                )
            }
        })
        HorizontalPagerIndicator(
            pagerState = pagerState,
            pageCount = cityInfoList.size,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}

fun getLocation(cityInfo: CityInfo?): String {
    if (cityInfo == null) return "CN101010100"
    return cityInfo.locationId.ifEmpty {
        cityInfo.location
    }
}
