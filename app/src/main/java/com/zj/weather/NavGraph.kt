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

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zj.weather.view.city.CityListPage
import com.zj.weather.view.city.viewmodel.CityListViewModel
import com.zj.weather.view.list.WeatherListPage
import com.zj.weather.view.list.viewmodel.WeatherListViewModel
import com.zj.weather.view.season.SeasonPage
import com.zj.weather.view.weather.WeatherViewPager
import com.zj.weather.view.weather.viewmodel.WeatherViewModel


@OptIn(
    ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun NavGraph(
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
            WeatherViewPager(
                weatherViewModel = weatherViewModel,
                toCityList = actions.toCityList,
                toWeatherList = actions.toWeatherList,
                toSeason = actions.toSeason
            )
        }
        searchComposable(PlayDestinations.WEATHER_LIST_ROUTE) {
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
        playComposable(PlayDestinations.CITY_LIST_ROUTE) {
            val cityListViewModel = hiltViewModel<CityListViewModel>()
            CityListPage(
                cityListViewModel = cityListViewModel,
                onBack = actions.upPress,
                toWeatherDetails = actions.upPress
            )
        }
        contentComposable(PlayDestinations.SEASON_PAGE_ROUTE) {
            SeasonPage()
        }
    }
}

object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val WEATHER_LIST_ROUTE = "weather_list_route"
    const val CITY_LIST_ROUTE = "city_list_route"
    const val SEASON_PAGE_ROUTE = "season_page_route"
}

@ExperimentalAnimationApi
fun NavGraphBuilder.playComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Left)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right)
        },
        content = content,
    )
}

@ExperimentalAnimationApi
fun NavGraphBuilder.searchComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            slideIntoContainer(AnimatedContentScope.SlideDirection.Up)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
        },
        content = content,
    )
}

@ExperimentalAnimationApi
fun NavGraphBuilder.contentComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = {
            scaleIn()
        },
        exitTransition = {
            scaleOut()
        },
        content = content,
    )
}

/**
 * 对应用程序中的导航操作进行建模。
 */
class PlayActions(navController: NavHostController) {

    val toWeatherList: () -> Unit = {
        navController.navigate(PlayDestinations.WEATHER_LIST_ROUTE)
    }

    val toCityList: () -> Unit = {
        navController.navigate(PlayDestinations.CITY_LIST_ROUTE)
    }

    val toSeason: () -> Unit = {
        navController.navigate(PlayDestinations.SEASON_PAGE_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.popBackStack()
    }

}