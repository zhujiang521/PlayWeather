package com.zj.weather.ui.view.list

import android.content.Context
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.MainViewModel
import com.zj.weather.R
import com.zj.weather.utils.showToast
import com.zj.weather.utils.swipe.SwipeDeleteLayout


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
                    CityItem(context, locationBean, toWeatherDetails)
                }
            }
        } else {
            NoContent(tip = stringResource(id = R.string.add_location_warn2))
        }
    }
}

@Composable
private fun CityItem(
    context: Context,
    locationBean: GeoBean.LocationBean,
    toWeatherDetails: (GeoBean.LocationBean) -> Unit,
) {
    SwipeDeleteLayout(childContent = {
        Column(modifier = Modifier.fillMaxHeight()) {
            Card(
                modifier = Modifier
                    .weight(1f), shape = RoundedCornerShape(0.dp, 5.dp, 5.dp, 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Color.Red)
                        .clickable {
                            showToast(context, "Collection")
                        }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Collection", fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }) {
        Column {
            Card(shape = RoundedCornerShape(5.dp)) {
                Text(text = locationBean.name, modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.background)
                    .clickable {
                        toWeatherDetails(locationBean)
                    }
                    .padding(10.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}