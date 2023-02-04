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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.zj.model.*
import com.zj.model.city.GeoBean
import com.zj.model.room.entity.CityInfo
import com.zj.utils.lce.ErrorContent
import com.zj.utils.lce.NoContent
import com.zj.utils.view.showToast
import com.zj.weather.R
import com.zj.weather.view.list.viewmodel.WeatherListViewModel
import com.zj.weather.view.list.widget.SearchBar
import com.zj.weather.view.list.widget.WeatherCityItem

@Composable
fun WeatherListPage(
    weatherListViewModel: WeatherListViewModel,
    onBack: () -> Unit,
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
            onBack()
        })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WeatherListPage(
    locationBeanState: PlayState<List<GeoBean.LocationBean>>,
    onBack: () -> Unit,
    onSearchCity: (String) -> Unit,
    onErrorClick: () -> Unit,
    toWeatherDetails: (CityInfo) -> Unit,
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.page_margin))
            .imeNestedScroll()
    ) {
        SearchBar(onBack) { city ->
            if (city.isNotEmpty()) {
                onSearchCity(city)
            } else {
                // 搜索城市为空，提醒用户输入城市名称
                showToast(context = context, R.string.city_list_search_hint)
            }
        }
        when (locationBeanState) {
            is PlayError -> {
                ErrorContent(onErrorClick = onErrorClick)
            }
            is PlayNoContent -> {
                NoContent(tip = locationBeanState.reason)
            }
            PlayLoading, is PlaySuccess -> {
                val listState = rememberLazyListState()
                LazyColumn(state = listState) {
                    if (locationBeanState is PlaySuccess) {
                        items(locationBeanState.data) { locationBean ->
                            WeatherCityItem(locationBean, toWeatherDetails)
                        }
                    } else {
                        items(10) {
                            WeatherCityItem(GeoBean.LocationBean(), toWeatherDetails)
                        }
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