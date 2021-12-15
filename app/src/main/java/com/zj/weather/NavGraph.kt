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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zj.weather.common.PlayActions
import com.zj.weather.common.PlayDestinations
import com.zj.weather.common.composable
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.city.CityListPage
import com.zj.weather.ui.view.city.viewmodel.CityListViewModel
import com.zj.weather.ui.view.list.WeatherListPage
import com.zj.weather.ui.view.list.viewmodel.WeatherListViewModel
import com.zj.weather.ui.view.weather.WeatherViewPager
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel


@OptIn(
    ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun NavGraph(
    defaultCityInfo: CityInfo?,
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE,
) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { PlayActions(navController) }
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(PlayDestinations.HOME_PAGE_ROUTE) {
            val weatherViewModel = hiltViewModel<WeatherViewModel>()
            LaunchedEffect(Unit) {
                if (defaultCityInfo != null) {
                    weatherViewModel.updateCityInfoIndex(cityInfo = defaultCityInfo)
                }
                weatherViewModel.refreshCityList()
            }
            WeatherViewPager(
                weatherViewModel = weatherViewModel,
                toCityList = actions.toCityList,
                toWeatherList = actions.toWeatherList
            )
        }
        composable(PlayDestinations.WEATHER_LIST_ROUTE) {
            val weatherListViewModel = hiltViewModel<WeatherListViewModel>()
            LaunchedEffect(Unit) {
                weatherListViewModel.getGeoTopCity()
            }
            WeatherListPage(
                weatherListViewModel = weatherListViewModel,
                onBack = actions.upPress,
                toWeatherDetails = actions.upPress
            )
        }
        composable(PlayDestinations.CITY_LIST_ROUTE) {
            val cityListViewModel = hiltViewModel<CityListViewModel>()
            LaunchedEffect(Unit) {
                cityListViewModel.refreshCityList()
            }
            CityListPage(
                cityListViewModel = cityListViewModel,
                onBack = actions.upPress,
                toWeatherDetails = actions.upPress
            )
        }
    }
}