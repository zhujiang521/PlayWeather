package com.zj.weather.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.zj.weather.MainViewModel
import com.zj.weather.common.PlayActions
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.list.DrawIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Composable
fun WeatherViewPager(
    mainViewModel: MainViewModel,
    coroutineScope: CoroutineScope,
    actions: PlayActions,
    initialPage: Int = 0,
    cityInfoList: List<CityInfo>,
    pagerState: PagerState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (initialPage > 0 && initialPage < pagerState.pageCount) {
            coroutineScope.launch {
                pagerState.scrollToPage(initialPage)
                mainViewModel.onSearchCityInfoChanged(0)
            }
        }
        HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
            WeatherPage(
                mainViewModel, cityInfoList[page],
                onErrorClick = {
                    mainViewModel.getWeather(getLocation(cityInfoList[page]))
                },
                cityList = {
                    actions.toCityList()
                }) {
                actions.toWeatherList()
            }
        }
        DrawIndicator(
            pagerState = pagerState,
            hasCurrentPosition = mainViewModel.hasLocation()
        )
    }
}

fun getLocation(
    cityInfo: CityInfo?
): String {
    if (cityInfo == null) return "CN101010100"
    return if (cityInfo.locationId.isNotEmpty()) {
        cityInfo.locationId
    } else {
        cityInfo.location
    }
}
