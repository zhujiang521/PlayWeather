package com.zj.weather.ui.view.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.MainViewModel
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo


@Composable
fun WeatherListPage(
    mainViewModel: MainViewModel,
    toWeatherDetails: (GeoBean.LocationBean) -> Unit,
) {
    val locationBeanList by mainViewModel.locationBeanList.observeAsState(listOf())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        val listState = rememberLazyListState()
        SearchBar { city ->
            mainViewModel.getGeoCityLookup(city)
        }
        if (locationBeanList.isNotEmpty()) {
            LazyColumn(state = listState) {
                items(locationBeanList) { locationBean ->
                    Text(text = locationBean.name, modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .clickable {
                            mainViewModel.onSearchCityInfoChanged(
                                0
                            )
                            toWeatherDetails(locationBean)
                        })
                }
            }
        } else {
            NoContent(tip = "查询的数据或地区不存在。")
        }
    }
}