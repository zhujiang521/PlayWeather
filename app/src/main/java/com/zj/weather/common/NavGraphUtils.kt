package com.zj.weather.common

import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.*
import com.google.accompanist.navigation.animation.composable

object PlayDestinations {
    const val HOME_PAGE_ROUTE = "home_page_route"
    const val WEATHER_LIST_ROUTE = "weather_list_route"
    const val CITY_LIST_ROUTE = "city_list_route"
}

@ExperimentalAnimationApi
fun NavGraphBuilder.composable(
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
//        exitTransition = {
//            slideOutOfContainer(AnimatedContentScope.SlideDirection.Right)
//        },
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
//        exitTransition = {
//            slideOutOfContainer(AnimatedContentScope.SlideDirection.Down)
//        },
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

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

}