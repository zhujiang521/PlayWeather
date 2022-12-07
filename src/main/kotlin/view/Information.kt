package view

import AppViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.weather.WeatherNowBean

/**
 * 主页面左边
 */
@Composable
fun Information(appViewModel: AppViewModel, nowBaseBean: WeatherNowBean.NowBaseBean?) {
    var showSearch by remember { mutableStateOf(false) }
    val currentCity by appViewModel.currentCity.collectAsState("北京")
    println("currentCityId:$currentCity")
    Box(
        Modifier.fillMaxHeight().width(300.dp).padding(end = 10.dp)
    ) {
        WeatherDetails(
            Modifier.fillMaxSize()
                .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
            nowBaseBean.apply {
                nowBaseBean?.city = currentCity
            }
        ) {
            showSearch = true
        }

        AnimatedVisibility(
            visible = showSearch,
            enter = slideInHorizontally(),
            exit = slideOutHorizontally()
        ) {
            SearchCity(
                Modifier.fillMaxSize()
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                appViewModel
            ) {
                showSearch = false
            }
        }

    }
}