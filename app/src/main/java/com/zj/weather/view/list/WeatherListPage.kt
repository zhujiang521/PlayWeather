package com.zj.weather.view.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zj.model.city.GeoBean
import com.zj.weather.R
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.utils.lce.LcePage
import com.zj.utils.lce.NoContent
import com.zj.model.room.entity.CityInfo
import com.zj.weather.view.list.viewmodel.WeatherListViewModel
import com.zj.weather.view.list.widget.SearchBar
import com.zj.weather.view.list.widget.WeatherCityItem
import com.zj.utils.showToast

@Composable
fun WeatherListPage(
    weatherListViewModel: WeatherListViewModel,
    onBack: () -> Unit,
    toWeatherDetails: () -> Unit,
) {
    val locationBeanState by weatherListViewModel.locationBeanList.observeAsState(PlayLoading)
    WeatherListPage(
        locationBeanState = locationBeanState,
        onBack = onBack,
        onSearchCity = { cityName ->
            weatherListViewModel.getGeoCityLookup(cityName)
        },
        onErrorClick = {
            weatherListViewModel.getGeoTopCity()
        },
        toWeatherDetails = { cityInfo ->
            weatherListViewModel.insertCityInfo(cityInfo)
            toWeatherDetails()
        })
}

@Composable
fun WeatherListPage(
    locationBeanState: PlayState<List<GeoBean.LocationBean>>,
    onBack: () -> Unit,
    onSearchCity: (String) -> Unit,
    onErrorClick: () -> Unit,
    toWeatherDetails: (CityInfo) -> Unit,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(onBack) { city ->
            if (city.isNotEmpty()) {
                onSearchCity(city)
            } else {
                // 搜索城市为空，提醒用户输入城市名称
                showToast(context = context, R.string.city_list_search_hint)
            }
        }
        Spacer(Modifier.height(10.dp))
        LcePage(playState = locationBeanState, onErrorClick = onErrorClick) { locationBeanList ->
            if (locationBeanList.isEmpty()){
                NoContent()
            }else {
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    state = listState
                ) {
                    items(locationBeanList) { locationBean ->
                        WeatherCityItem(locationBean, toWeatherDetails)
                    }
                }
            }
        }

    }
}

@Preview(showBackground = false, name = "城市列表")
@Composable
fun WeatherListPagePreview() {
    val list = arrayListOf<GeoBean.LocationBean>()
    for (index in 0..11) {
        val bean = GeoBean.LocationBean()
        bean.name = "某地"
        bean.adm1 = "某某省"
        bean.adm2 = "前列县"
        list.add(bean)
    }
    val playState = PlaySuccess(list)
    WeatherListPage(playState, {}, {}, {}, {})
}

@Preview(showBackground = false, name = "城市列表空数据")
@Composable
fun WeatherListPageNoContentPreview() {
    val playState = PlaySuccess(listOf<GeoBean.LocationBean>())
    WeatherListPage(playState, {}, {}, {}, {})
}

@Preview(showBackground = false, name = "城市列表错误页面")
@Composable
fun WeatherListPageErrorPreview() {
    val playState = PlayError(NullPointerException())
    WeatherListPage(playState, {}, {}, {}, {})
}