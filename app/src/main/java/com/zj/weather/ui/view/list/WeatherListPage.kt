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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.MainViewModel
import com.zj.weather.R
import com.zj.weather.utils.showToast


@Composable
fun WeatherListPage(
    mainViewModel: MainViewModel,
    toWeatherDetails: (GeoBean.LocationBean) -> Unit,
) {
    val locationBeanList by mainViewModel.locationBeanList.observeAsState(listOf())
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        val listState = rememberLazyListState()
        SearchBar { city ->
            if (city.isNotEmpty()) {
                mainViewModel.getGeoCityLookup(city)
            } else {
                // 搜索城市为空，提醒用户输入城市名称
                showToast(context = context, R.string.city_list_search_hint)
            }
        }
        if (locationBeanList.isNotEmpty()) {
            LazyColumn(state = listState) {
                items(locationBeanList) { locationBean ->
                    Text(text = locationBean.name, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            toWeatherDetails(locationBean)
                        }
                        .padding(10.dp))

                }
            }
        } else {
            NoContent(tip = stringResource(id = R.string.add_location_warn2))
        }
    }
}