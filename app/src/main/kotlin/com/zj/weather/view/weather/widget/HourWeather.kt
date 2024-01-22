package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.weather.WeatherHourlyBean
import com.zj.utils.view.ImageLoader
import com.zj.utils.weather.IconUtils
import com.zj.weather.R
import com.zui.animate.placeholder.placeholder

@Composable
fun HourWeather(hourlyBeanList: List<WeatherHourlyBean.HourlyBean>?) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.twenty_four_hour_title),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                val beanList = hourlyBeanList ?: buildHourItemList()
                items(beanList) { hourlyBean ->
                    HourWeatherItem(hourlyBean)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

private fun buildHourItemList(): List<WeatherHourlyBean.HourlyBean?> {
    val hourlyBeanList: ArrayList<WeatherHourlyBean.HourlyBean?> = arrayListOf()
    for (index in 0..23) {
        hourlyBeanList.add(null)
    }
    return hourlyBeanList
}

@Composable
private fun HourWeatherItem(hourlyBean: WeatherHourlyBean.HourlyBean?) {
    Column(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourlyBean?.fxTime ?: "",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.placeholder(hourlyBean)
        )
        ImageLoader(
            data = IconUtils.getWeatherIcon(hourlyBean?.icon),
            modifier = Modifier
                .padding(top = 7.dp)
                .placeholder(hourlyBean)
        )
        Text(
            text = "${hourlyBean?.temp}℃",
            modifier = Modifier
                .padding(top = 7.dp)
                .placeholder(hourlyBean),
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview(showBackground = false, name = "小时item")
@Composable
fun HourWeatherItemPreview() {
    val hourlyBean = WeatherHourlyBean.HourlyBean()
    hourlyBean.fxTime = "21时"
    hourlyBean.temp = "15"
    hourlyBean.icon = "100"
    HourWeatherItem(hourlyBean = hourlyBean)
}