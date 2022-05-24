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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.indices.WeatherLifeIndicesBean
import com.zj.utils.view.ImageLoader
import com.zj.weather.R


@Composable
fun LifeWeatherContent(weatherLifeList: List<WeatherLifeIndicesBean.WeatherLifeIndicesItem>?) {
    if (weatherLifeList.isNullOrEmpty()) return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
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
                    modifier, R.drawable.ic_sport, R.string.life_sport, weatherLifeList[0].category
                )
                WeatherLifeItem(
                    modifier, R.drawable.ic_car, R.string.life_car, weatherLifeList[1].category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_clothes,
                    R.string.life_clothes,
                    weatherLifeList[2].category
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
                    modifier, R.drawable.ic_uv, R.string.life_uv, weatherLifeList[3].category
                )
                WeatherLifeItem(
                    modifier,
                    R.drawable.ic_travel,
                    R.string.life_travel,
                    weatherLifeList[4].category
                )
                WeatherLifeItem(
                    modifier, R.drawable.ic_cold, R.string.life_cold, weatherLifeList[5].category
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = stringResource(id = R.string.data_source),
        fontSize = 12.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 35.dp)
    )
}

@Composable
fun WeatherLifeItem(modifier: Modifier, imgRes: Int, titleId: Int, value: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        ImageLoader(data = imgRes, modifier = Modifier.size(30.dp))

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = stringResource(id = titleId), fontSize = 12.sp)
            Text(
                text = value,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary
            )
        }

    }
}
