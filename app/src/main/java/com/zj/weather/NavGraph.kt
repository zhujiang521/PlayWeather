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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.WeatherPage
import com.zj.weather.ui.view.list.DrawIndicator
import com.zj.weather.ui.view.list.WeatherListPage
import com.zj.weather.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

private const val TAG = "NavGraph"

object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val WEATHER_LIST_ROUTE = "weather_list_route"
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE,
    mainViewModel: MainViewModel
) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { PlayActions(navController) }
    val context = LocalContext.current
    val cityInfoDao = PlayWeatherDatabase.getDatabase(context).cityInfoDao()
    val coroutineScope = rememberCoroutineScope()
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setComposable(
            PlayDestinations.HOME_PAGE_ROUTE,
        ) {
            val searchCityInfo by mainViewModel.searchCityInfo.observeAsState()
            var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
            if (cityInfoList.isNullOrEmpty()) {
                cityInfoList = listOf(
                    CityInfo(
                        location = "CN101010100",
                        name = stringResource(id = R.string.default_location)
                    )
                )
            } else {
                Log.e(TAG, "NavGraph: cityInfoList:$cityInfoList")
            }
            val initialPage = if (searchCityInfo == 2) {
                cityInfoList.size - 1
            } else {
                0
            }
            Log.e(TAG, "NavGraph: cityInfoList initialPage:$initialPage")
            val isLocation = runBlocking { cityInfoDao.getIsLocationList() }
            val pagerState = rememberPagerState(initialPage = initialPage)
            mainViewModel.getWeather(cityInfoList[pagerState.currentPage].location)
            Box(modifier = Modifier.fillMaxSize()) {
                if (initialPage == cityInfoList.size - 1) {
                    coroutineScope.launch {
                        pagerState.scrollToPage(initialPage)
                        mainViewModel.onSearchCityInfoChanged(0)
                    }
                }
                HorizontalPager(count = cityInfoList.size, state = pagerState) { page ->
                    WeatherPage(mainViewModel, cityInfoList[page]) {
                        actions.toWeatherList()
                    }
                }
                DrawIndicator(pagerState = pagerState, hasCurrentPosition = isLocation.isNotEmpty())
            }
        }
        setComposable(
            PlayDestinations.WEATHER_LIST_ROUTE,
        ) {
            mainViewModel.getGeoTopCity()
            WeatherListPage(mainViewModel = mainViewModel, toWeatherDetails = { locationBean ->
                coroutineScope.launch(Dispatchers.IO) {
                    val hasLocation = cityInfoDao.getHasLocation(locationBean.name)
                    val cityInfo = CityInfo(
                        location = "${locationBean.lon},${
                            locationBean.lat
                        }",
                        name = locationBean.name
                    )
                    if (hasLocation.isNullOrEmpty()) {
                        cityInfoDao.insert(cityInfo)
                        withContext(Dispatchers.Main) {
                            mainViewModel.onSearchCityInfoChanged(2)
                            actions.upPress()
                        }
                    } else {
                        showToast(context, R.string.add_location_warn)
                    }
                }
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

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

}


