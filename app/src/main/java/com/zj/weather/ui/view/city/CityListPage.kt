package com.zj.weather.ui.view.city

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.R
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.view.list.NoContent
import com.zj.weather.utils.swipe.SwipeDeleteLayout

@Composable
fun CityListPage(
    cityInfoList: List<CityInfo>,
    onBack: () -> Unit,
    toWeatherDetails: (GeoBean.LocationBean) -> Unit,
    onDeleteListener: (CityInfo) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
    ) {
        Spacer(Modifier.height(30.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                modifier = Modifier.wrapContentWidth(Alignment.End),
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back"
                )
            }
            Text(
                text = stringResource(id = R.string.city_title),
                fontSize = 25.sp,
                modifier = Modifier.padding(10.dp)
            )
        }

        Spacer(Modifier.height(10.dp))
        val listState = rememberLazyListState()
        if (cityInfoList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 10.dp),
                state = listState
            ) {
                items(cityInfoList) { cityInfo ->
                    CityItem(
                        cityInfo,
                        cityInfo.isLocation != 1,
                        toWeatherDetails,
                        onDeleteListener
                    )
                }
            }
        } else {
            NoContent(tip = stringResource(id = R.string.add_location_warn2))
        }
    }
}

@Composable
private fun CityItem(
    cityInfo: CityInfo,
    isShowDelete: Boolean = true,
    toWeatherDetails: (GeoBean.LocationBean) -> Unit,
    onDeleteListener: (CityInfo) -> Unit
) {
    SwipeDeleteLayout(isShowChild = isShowDelete, childContent = {
        Column(modifier = Modifier.fillMaxHeight()) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(0.dp, 5.dp, 5.dp, 0.dp),
                backgroundColor = MaterialTheme.colors.primaryVariant
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(color = Color.Red)
                        .clickable {
                            onDeleteListener(cityInfo)
                        }
                        .padding(5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.city_delete),
                        fontSize = 14.sp,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }) {
        Column {
            Card(shape = RoundedCornerShape(5.dp)) {
                Text(text = cityInfo.name, modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.primaryVariant)
                    .clickable {
                        val locationBean = GeoBean.LocationBean()
                        toWeatherDetails(locationBean)
                    }
                    .padding(10.dp))
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}