package com.zj.weather.ui.view.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.R
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.common.lce.LcePage
import com.zj.weather.common.lce.NoContent
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.list.viewmodel.WeatherListViewModel
import com.zj.weather.ui.view.list.widget.CityItem
import com.zj.weather.ui.view.list.widget.SearchBar
import com.zj.weather.utils.showToast

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
    LcePage(playState = locationBeanState, onErrorClick = onErrorClick) { locationBeanList ->
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
            if (locationBeanList.isNotEmpty()) {
                val listState = rememberLazyListState()
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    state = listState
                ) {
                    items(locationBeanList) { locationBean ->
                        CityItem(locationBean, toWeatherDetails)
                    }
                }
            } else {
                NoContent(tip = stringResource(id = R.string.add_location_warn2))
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