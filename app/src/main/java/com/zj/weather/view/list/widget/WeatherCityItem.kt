package com.zj.weather.view.list.widget

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zj.model.city.GeoBean
import com.zj.model.room.entity.CityInfo
import com.zj.utils.dialog.ShowDialog
import com.zj.utils.dialog.ShowWarnDialog
import com.zj.weather.R

@Composable
fun WeatherCityItem(
    locationBean: GeoBean.LocationBean,
    toWeatherDetails: (CityInfo) -> Unit,
) {
    val warnDialog = remember { mutableStateOf(false) }
    val alertDialog = remember { mutableStateOf(false) }
    val hasLocation = locationBean.hasLocation
    Column {
        Card(shape = RoundedCornerShape(5.dp)) {
            Text(
                text = "${locationBean.adm1} ${locationBean.adm2} ${locationBean.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colors.primaryVariant)
                    .clickable {
                        if (!hasLocation) {
                            alertDialog.value = true
                        } else {
                            warnDialog.value = true
                        }
                    }
                    .padding(horizontal = 10.dp, vertical = 15.dp),
                color = if (!hasLocation) Color.Unspecified else Color.Gray)
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
        ShowWarnDialog(
            alertDialog = warnDialog,
            titleId = R.string.city_dialog_title,
            contentId = R.string.add_location_warn
        )
    }
}