package com.zj.weather.ui.view.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.MainViewModel
import com.zj.weather.R
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.lce.LcePage
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.showToast


@Composable
fun WeatherListPage(
    mainViewModel: MainViewModel,
    onBack: () -> Unit,
    onErrorClick: () -> Unit,
    toWeatherDetails: (CityInfo) -> Unit,
) {
    val locationBeanState by mainViewModel.locationBeanList.observeAsState(PlayLoading)
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        val listState = rememberLazyListState()
        SearchBar(onBack) { city ->
            if (city.isNotEmpty()) {
                mainViewModel.getGeoCityLookup(city)
            } else {
                // 搜索城市为空，提醒用户输入城市名称
                showToast(context = context, R.string.city_list_search_hint)
            }
        }
        Spacer(Modifier.height(10.dp))
        LcePage(playState = locationBeanState, onErrorClick = onErrorClick) { locationBeanList ->
            LazyColumn(
                modifier = Modifier.padding(horizontal = 5.dp),
                state = listState
            ) {
                items(locationBeanList) { locationBean ->
                    CityItem(locationBean, toWeatherDetails)
                }
            }
        }
    }
}

@Composable
private fun CityItem(
    locationBean: GeoBean.LocationBean,
    toWeatherDetails: (CityInfo) -> Unit,
) {
    val alertDialog = remember { mutableStateOf(false) }

    Column {
        Card(shape = RoundedCornerShape(5.dp)) {
            Text(
                text = "${locationBean.adm1} ${locationBean.adm2} ${locationBean.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.primaryVariant)
                    .clickable {
                        alertDialog.value = true
                    }
                    .padding(horizontal = 10.dp, vertical = 15.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        ShowDialog(alertDialog = alertDialog, "${locationBean.adm2} ${locationBean.name}") {
            toWeatherDetails(
                CityInfo(
                    location = "${locationBean.lon},${
                        locationBean.lat
                    }",
                    name = locationBean.name,
                    province = locationBean.adm1,
                    city = locationBean.adm2,
                    locationId = "CN${locationBean.id}"
                )
            )
        }
    }
}