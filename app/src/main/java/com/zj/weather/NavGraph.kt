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

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zj.weather.ui.view.WeatherPage
import com.zj.weather.ui.view.city.CityListPage
import com.zj.weather.ui.view.list.DrawIndicator
import com.zj.weather.ui.view.list.WeatherListPage
import kotlinx.coroutines.launch

private const val TAG = "NavGraph"

object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val WEATHER_LIST_ROUTE = "weather_list_route"
    const val CITY_LIST_ROUTE = "city_list_route"
}

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
        setComposable(
            PlayDestinations.HOME_PAGE_ROUTE,
        ) {
            mainViewModel.getCityList()
            val cityInfoList by mainViewModel.cityInfoList.observeAsState(listOf())
            val initialPage by mainViewModel.searchCityInfo.observeAsState(0)
            Log.e(TAG, "NavGraph: cityInfoList initialPage:$initialPage")
            val pagerState = rememberPagerState(initialPage = initialPage)
            if (cityInfoList.isNotEmpty()) {
                val index = if (pagerState.currentPage > cityInfoList.size - 1) {
                    0
                } else pagerState.currentPage
                mainViewModel.getWeather(cityInfoList[index].location)
            }
            Box(modifier = Modifier.fillMaxSize()) {
                if (initialPage > 0) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(initialPage)
                        mainViewModel.onSearchCityInfoChanged(0)
                    }
                }
                HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
                    WeatherPage(mainViewModel, cityInfoList[page], cityList = {
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
        setComposable(
            PlayDestinations.WEATHER_LIST_ROUTE,
        ) {
            mainViewModel.getGeoTopCity()
            WeatherListPage(
                mainViewModel = mainViewModel,
                onBack = actions.upPress,
                toWeatherDetails = { cityInfo ->
                    mainViewModel.insertCityInfo(cityInfo) {
                        val count = mainViewModel.getCount()
                        mainViewModel.onSearchCityInfoChanged(count - 1)
                        actions.upPress()
                    }
                })
        }
        setComposable(
            PlayDestinations.CITY_LIST_ROUTE,
        ) {
            mainViewModel.getCityList()
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

@ExperimentalAnimationApi
fun NavGraphBuilder.setComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
        enterTransition = { _, _ ->
            // Let's make for a really long fade in
            expandVertically(animationSpec = tween(300))
        },
        exitTransition = { _, _ ->
            // Let's make for a really long fade in
            shrinkOut(animationSpec = tween(300))
        },
        content = content,
//        popEnterTransition = { _, _ ->
//            shrinkOut(animationSpec = tween(2000))
//        },
//        popExitTransition = { _, _ ->
//            slideOutOfContainer(
//                AnimatedContentScope.SlideDirection.Right,
//                animationSpec = tween(300)
//            )
//        }
    )
}

/**
 * 对应用程序中的导航操作进行建模。
 */
class PlayActions(navController: NavHostController) {

    val toHomePage: () -> Unit = {
        navController.navigate(PlayDestinations.HOME_PAGE_ROUTE)
    }

    val toWeatherList: () -> Unit = {
        navController.navigate(PlayDestinations.WEATHER_LIST_ROUTE)
    }

    val toCityList: () -> Unit = {
        navController.navigate(PlayDestinations.CITY_LIST_ROUTE)
    }

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

}


