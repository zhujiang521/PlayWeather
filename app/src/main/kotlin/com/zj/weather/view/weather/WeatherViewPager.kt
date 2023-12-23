@file:OptIn(ExperimentalFoundationApi::class)

package com.zj.weather.view.weather

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import com.zj.model.room.entity.CityInfo
import com.zj.utils.XLog
import com.zj.utils.defaultCityState
import com.zj.utils.lce.NoContent
import com.zj.utils.view.HorizontalPagerIndicator
import com.zj.weather.permission.FeatureThatRequiresLocationPermissions
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.view.weather.widget.HeaderAction
import kotlinx.coroutines.delay

@ExperimentalPermissionsApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toCityMap: (Double, Double) -> Unit,
    toWeatherList: () -> Unit
) {
    val cityInfoList by weatherViewModel.cityInfoList.collectAsState(initial = arrayListOf())
    if (cityInfoList.isEmpty()) {
        FeatureThatRequiresLocationPermissions(weatherViewModel)
        NoCityContent(toWeatherList, toCityList)
    } else {
        val pagerState = rememberPagerState(
            initialPage = 0, initialPageOffsetFraction = 0f
        ) {
            cityInfoList.size
        }
        WeatherViewPager(
            weatherViewModel, cityInfoList, pagerState, toCityList, toCityMap, toWeatherList
        )
        CurrentPageEffect(pagerState, cityInfoList, weatherViewModel)
        Weather(cityInfoList, pagerState)
    }
}

@ExperimentalPermissionsApi
@Composable
private fun Weather(
    cityInfoList: List<CityInfo>,
    pagerState: PagerState
) {
    val value = defaultCityState.value ?: return
    var indexOf = -1
    cityInfoList.forEachIndexed { index, cityInfo ->
        if (value.locationId == cityInfo.locationId && value.location == cityInfo.location) {
            indexOf = index
            return@forEachIndexed
        }
    }
    if (indexOf < 0 || indexOf == pagerState.currentPage) return
    LaunchedEffect(Unit) {
        pagerState.scrollToPage(indexOf)
        XLog.d("scrollToPage:$indexOf")
        defaultCityState.value = null
    }
}

@Composable
fun CurrentPageEffect(
    pagerState: PagerState, cityInfoList: List<CityInfo>, weatherViewModel: WeatherViewModel
) {
    if (pagerState.isScrollInProgress) {
        return
    }
    val index = if (pagerState.currentPage > cityInfoList.size - 1) 0 else pagerState.currentPage
    LaunchedEffect(pagerState.currentPage) {
        delay(300L)
        val cityInfo = cityInfoList[index]
        weatherViewModel.getWeather(cityInfo)
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
        modifier = Modifier.fillMaxSize()
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

            val pullRefreshState = rememberPullRefreshState(isRefreshing,
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
                .padding(bottom = 25.dp),
        )
    }
}