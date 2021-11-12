package com.zj.weather.ui.view.list.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.R
import com.zj.weather.common.dialog.ShowDialog
import com.zj.weather.room.entity.CityInfo

@Composable
fun CityItem(
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
        ShowDialog(
            alertDialog = alertDialog,
            title = stringResource(id = R.string.city_dialog_title),
            content = stringResource(
                id = R.string.city_dialog_content,
                "${locationBean.adm2} ${locationBean.name}"
            ), cancelString = stringResource(id = R.string.city_dialog_cancel),
            confirmString = stringResource(id = R.string.city_dialog_confirm)
        ) {
            toWeatherDetails(
                CityInfo(
                    location = "${locationBean.lon},${
                        locationBean.lat
                    }",
                    name = locationBean.name,
                    province = locationBean.adm1,
                    city = locationBean.adm2,
                    locationId = "CN${locationBean.id}",
                    isIndex = 1
                )
            )
        }
    }
}