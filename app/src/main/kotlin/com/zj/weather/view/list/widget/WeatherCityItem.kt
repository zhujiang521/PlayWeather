package com.zj.weather.view.list.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.city.GeoBean
import com.zj.model.room.entity.CityInfo
import com.zj.utils.defaultCityState
import com.zj.utils.dialog.ShowDialog
import com.zj.utils.dialog.ShowWarnDialog
import com.zj.weather.R
import com.zui.animate.placeholder.placeholder

@Composable
fun WeatherCityItem(
    locationBean: GeoBean.LocationBean?,
    toWeatherDetails: ((CityInfo) -> Unit)?,
) {
    val warnDialog = remember { mutableStateOf(false) }
    val alertDialog = remember { mutableStateOf(false) }
    val hasLocation = locationBean?.hasLocation ?: false
    Card(shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clickable {
                if (!hasLocation) {
                    alertDialog.value = true
                } else {
                    warnDialog.value = true
                }
            }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.placeholder(locationBean),
                    text = locationBean?.name ?: "",
                    color = if (!hasLocation) Color.Unspecified else Color.Gray,
                    fontSize = 18.sp
                )

                Text(
                    text = "${locationBean?.adm1} ${locationBean?.adm2}",
                    modifier = Modifier
                        .padding(top = 3.dp)
                        .placeholder(locationBean),
                    color = if (!hasLocation) Color.Unspecified else Color.Gray
                )
            }

            Text(
                modifier = Modifier.placeholder(locationBean),
                text = "${stringResource(id = R.string.city_rank)}${locationBean?.rank}",
                color = if (!hasLocation) Color.Unspecified else Color.Gray
            )
        }


    }
    ShowDialog(
        alertDialog = alertDialog,
        title = stringResource(id = R.string.city_dialog_title),
        content = stringResource(
            id = R.string.city_dialog_content, "${locationBean?.adm2} ${locationBean?.name}"
        ),
        cancelString = stringResource(id = R.string.city_dialog_cancel),
        confirmString = stringResource(id = R.string.city_dialog_confirm)
    ) {
        val cityInfo = CityInfo(
            location = "${locationBean?.lon},${
                locationBean?.lat
            }",
            name = locationBean?.name ?: "山西省",
            province = locationBean?.adm1 ?: "长治市",
            city = locationBean?.adm2 ?: "潞城区",
            locationId = "CN${locationBean?.id}",
            lat = locationBean?.lat ?: "",
            lon = locationBean?.lon ?: "",
        )
        defaultCityState.value = cityInfo
        toWeatherDetails?.let {
            it(
                cityInfo
            )
        }
    }
    ShowWarnDialog(
        alertDialog = warnDialog,
        titleId = R.string.city_dialog_title,
        contentId = R.string.add_location_warn
    )
}