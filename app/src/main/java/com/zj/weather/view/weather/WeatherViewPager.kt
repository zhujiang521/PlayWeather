package com.zj.weather.view.weather

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zj.model.room.entity.CityInfo
import com.zj.utils.XLog
import com.zj.utils.lce.NoContent
import com.zj.utils.weather.getCityIndex
import com.zj.weather.permission.FeatureThatRequiresLocationPermissions
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.view.weather.widget.HeaderAction
import kotlinx.coroutines.launch

@ExperimentalPermissionsApi
@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
    toSeason: () -> Unit
) {
    val cityInfoList by weatherViewModel.cityInfoList.observeAsState()
    val pagerState = rememberPagerState()
    if (cityInfoList == null || cityInfoList.isNullOrEmpty()) {
        XLog.w("Empty, refresh")
        if (pagerState.currentPage == 0) {
            FeatureThatRequiresLocationPermissions(weatherViewModel)
        }
        NoCityContent(toWeatherList, toCityList)
    } else {
        CurrentPageEffect(pagerState, cityInfoList ?: arrayListOf(), weatherViewModel)
        WeatherViewPager(
            weatherViewModel,
            cityInfoList ?: arrayListOf(),
            pagerState,
            getCityIndex(cityInfoList),
            toCityList,
            toWeatherList,
            toSeason
        )
    }
}

@ExperimentalPagerApi
@Composable
fun CurrentPageEffect(
    pagerState: PagerState,
    cityInfoList: List<CityInfo>,
    weatherViewModel: WeatherViewModel
) {
    LaunchedEffect(pagerState.currentPage) {
        val index =
            if (pagerState.currentPage > cityInfoList.size - 1) 0 else pagerState.currentPage
        val cityInfo = cityInfoList[index]
        val location = getLocation(cityInfo = cityInfo)
        weatherViewModel.getWeather(location)
        XLog.w("Query initialPage")
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
                .statusBarsPadding(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HeaderAction(
                modifier = Modifier.weight(1f),
                cityListClick = toWeatherList,
                cityList = toCityList
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
@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    cityInfoList: List<CityInfo>,
    pagerState: PagerState,
    initialPage: Int,
    toCityList: () -> Unit,
    toWeatherList: () -> Unit,
    toSeason: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize()) {
        if (initialPage >= 0 && initialPage < pagerState.pageCount) {
            coroutineScope.launch {
                pagerState.scrollToPage(initialPage)
            }
        }
        HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
            val isRefreshing by weatherViewModel.isRefreshing.collectAsState()

            val pullRefreshState = rememberPullRefreshState(
                isRefreshing,
                { weatherViewModel.refresh(getLocation(cityInfoList[page])) })

            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    .clickable {
                        toSeason()
                    }) {
                WeatherPage(
                    weatherViewModel, cityInfoList[page],
                    onErrorClick = {
                        val location = getLocation(cityInfoList[page])
                        weatherViewModel.getWeather(location)
                    },
                    cityList = toCityList, cityListClick = toWeatherList
                )

                PullRefreshIndicator(
                    isRefreshing,
                    pullRefreshState,
                    Modifier.align(Alignment.TopCenter)
                )
            }
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
