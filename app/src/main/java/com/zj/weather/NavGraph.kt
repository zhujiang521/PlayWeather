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

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.zj.weather.ui.view.WeatherPage
import com.zj.weather.ui.view.list.WeatherListPage


object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val WEATHER_LIST_ROUTE = "weather_list_route"
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    startDestination: String = PlayDestinations.HOME_PAGE_ROUTE,
    mainViewModel: MainViewModel
) {
    val navController = rememberAnimatedNavController()
    val actions = remember(navController) { PlayActions(navController) }
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        setComposable(
            PlayDestinations.HOME_PAGE_ROUTE,
        ) {
            WeatherPage(mainViewModel) {
                actions.toWeatherList()
            }
        }
        setComposable(
            PlayDestinations.WEATHER_LIST_ROUTE,
        ) {
            WeatherListPage {

            }
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


