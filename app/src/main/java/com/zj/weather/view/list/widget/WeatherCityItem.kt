package com.zj.weather.view.list.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
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
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
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
    Card(
        shape = RoundedCornerShape(5.dp), modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .placeholder(
                visible = locationBean.adm1.isNullOrEmpty() &&
                        locationBean.adm2.isNullOrEmpty() &&
                        locationBean.name.isNullOrEmpty(),
                color = MaterialTheme.colors.primaryVariant,
                // optional, defaults to RectangleShape
                shape = RoundedCornerShape(4.dp),
                highlight = PlaceholderHighlight.shimmer(
                    highlightColor = Color.White,
                ),
            )
            .clickable {
                if (!hasLocation) {
                    alertDialog.value = true
                } else {
                    warnDialog.value = true
                }
            }
    ) {

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
                    text = locationBean.name ?: "",
                    color = if (!hasLocation) Color.Unspecified else Color.Gray, fontSize = 18.sp
                )

                Text(
                    text = "${locationBean.adm1} ${locationBean.adm2}",
                    modifier = Modifier.padding(top = 3.dp),
                    color = if (!hasLocation) Color.Unspecified else Color.Gray
                )
            }

            Text(
                text = "${stringResource(id = R.string.city_rank)}${locationBean.rank}",
                color = if (!hasLocation) Color.Unspecified else Color.Gray
            )
        }


    }
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
                name = locationBean.name ?: "山西省",
                province = locationBean.adm1 ?: "长治市",
                city = locationBean.adm2 ?: "潞城区",
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