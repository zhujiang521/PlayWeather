/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zj.weather

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.zj.weather.common.PlayActions
import com.zj.weather.common.PlayDestinations
import com.zj.weather.common.setComposable
import com.zj.weather.ui.view.WeatherViewPager
import com.zj.weather.ui.view.city.CityListPage
import com.zj.weather.ui.view.getLocation
import com.zj.weather.ui.view.list.WeatherListPage
import com.zj.weather.utils.XLog


@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE,
    mainViewModel: MainViewModel
) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { PlayActions(navController) }
    val coroutineScope = rememberCoroutineScope()
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setComposable(PlayDestinations.HOME_PAGE_ROUTE) {
            val cityInfoList by mainViewModel.cityInfoList.observeAsState()
            val initialPage by mainViewModel.searchCityInfo.observeAsState()
            val pagerState = rememberPagerState()
            cityInfoList?.apply {
                if (isNullOrEmpty()) return@apply
                val index = if (pagerState.currentPage > size - 1) {
                    0
                } else pagerState.currentPage
                val cityInfo = get(index)
                val location = getLocation(cityInfo = cityInfo)
                LaunchedEffect(location) {
                    mainViewModel.getWeather(location)
                    XLog.e("查询 initialPage:$initialPage")
                }
            }
            WeatherViewPager(
                mainViewModel, coroutineScope, actions, initialPage ?: 0,
                cityInfoList ?: mainViewModel.makeDefault(cityInfoList), pagerState
            )
        }
        setComposable(PlayDestinations.WEATHER_LIST_ROUTE) {
            mainViewModel.getGeoTopCity()
            WeatherListPage(
                mainViewModel = mainViewModel,
                onBack = actions.upPress,
                onErrorClick = {
                    mainViewModel.getGeoTopCity()
                },
                toWeatherDetails = { cityInfo ->
                    mainViewModel.insertCityInfo(cityInfo) {
                        val count = mainViewModel.getCount()
                        mainViewModel.onSearchCityInfoChanged(count - 1)
                        actions.upPress()
                    }
                })
        }
        setComposable(PlayDestinations.CITY_LIST_ROUTE) {
            mainViewModel.refreshCityList()
            val cityInfoList by mainViewModel.cityInfoList.observeAsState(listOf())
            CityListPage(
                cityInfoList = cityInfoList,
                onBack = actions.upPress,
                toWeatherDetails = {
                    mainViewModel.onSearchCityInfoChanged(cityInfoList.indexOf(it))
                    actions.upPress()
                },
                onDeleteListener = {
                    mainViewModel.deleteCityInfo(it)
                })
        }
    }
}