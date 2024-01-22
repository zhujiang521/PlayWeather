package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.indices.WeatherLifeIndicesBean
import com.zj.utils.view.ImageLoader
import com.zj.weather.R
import com.zui.animate.placeholder.placeholder


@Composable
fun LifeWeatherContent(weatherLifeList: List<WeatherLifeIndicesBean.WeatherLifeIndicesItem>?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        val weatherLifeIndicesItems = weatherLifeList ?: buildLifeItemList()
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.life_day_title),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            ) {
                val modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_sport,
                    R.string.life_sport,
                    weatherLifeIndicesItems[0]?.category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_car,
                    R.string.life_car,
                    weatherLifeIndicesItems[1]?.category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_clothes,
                    R.string.life_clothes,
                    weatherLifeIndicesItems[1]?.category
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            ) {
                val modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_uv,
                    R.string.life_uv,
                    weatherLifeIndicesItems[3]?.category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_travel,
                    R.string.life_travel,
                    weatherLifeIndicesItems[4]?.category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_cold,
                    R.string.life_cold,
                    weatherLifeIndicesItems[5]?.category
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

private fun buildLifeItemList(): List<WeatherLifeIndicesBean.WeatherLifeIndicesItem?> {
    val weatherLifeIndicesItems: ArrayList<WeatherLifeIndicesBean.WeatherLifeIndicesItem?> =
        arrayListOf()
    for (index in 0..5) {
        weatherLifeIndicesItems.add(null)
    }
    return weatherLifeIndicesItems
}

@Composable
fun WeatherLifeItem(modifier: Modifier, imgRes: Int, titleId: Int, value: String?) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        ImageLoader(data = imgRes, modifier = Modifier.size(30.dp))

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(
                text = stringResource(id = titleId),
                fontSize = 12.sp,
            )
            Text(
                text = value ?: "不错",
                modifier = Modifier
                    .padding(top = 5.dp)
                    .placeholder(value),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary
            )
        }

    }
}
