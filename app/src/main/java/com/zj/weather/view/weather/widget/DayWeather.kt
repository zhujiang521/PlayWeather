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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.weather.WeatherDailyBean
import com.zj.weather.R
import com.zj.utils.view.ImageLoader
import com.zj.utils.weather.IconUtils

@Composable
fun DayWeather(dayBeanList: List<WeatherDailyBean.DailyBean>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.seven_day_title),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            dayBeanList?.forEach { dailyBean ->
                Divider(modifier = Modifier.padding(horizontal = 10.dp))
                DayWeatherItem(dailyBean)
            }
        }
    }
}

@Composable
private fun DayWeatherItem(dailyBean: WeatherDailyBean.DailyBean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dailyBean.fxDate,
            modifier = Modifier
                .weight(1f)
                .padding(start = 3.dp),
            fontSize = 15.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        ImageLoader(
            data = IconUtils.getWeatherIcon(dailyBean.iconDay),
            modifier = Modifier
                .padding(start = 7.dp)
        )
        Spacer(modifier = Modifier.weight(1.2f))
        Text(
            text = "${dailyBean.tempMin ?: "0"}℃",
            modifier = Modifier
                .padding(start = 7.dp)
                .weight(2f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = "${dailyBean.tempMax ?: "0"}℃",
            modifier = Modifier
                .padding(start = 7.dp, end = 3.dp)
                .weight(2f),
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview(showBackground = false, name = "天item")
@Composable
fun DayWeatherItemPreview() {
    val dailyBean = WeatherDailyBean.DailyBean()
    dailyBean.fxDate = "周一"
    dailyBean.iconDay = "100"
    dailyBean.tempMin = "10"
    dailyBean.tempMax = "20"
    DayWeatherItem(dailyBean = dailyBean)
}
