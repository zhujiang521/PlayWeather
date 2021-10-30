package com.zj.weather.ui.view.weather

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.zj.weather.utils.IconUtils
import com.zj.weather.utils.ImageLoader

@Composable
fun DayWeather(dayBeanList: List<WeatherDailyBean.DailyBean>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .alpha(0.9f),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            dayBeanList?.forEach { dailyBean ->
                DayWeatherItem(dailyBean)
            }
        }
    }
}

@Composable
private fun DayWeatherItem(dailyBean: WeatherDailyBean.DailyBean) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(
            text = dailyBean.fxDate,
            modifier = Modifier.weight(1f),
            fontSize = 15.sp,
            color = MaterialTheme.colors.primary
        )
        Spacer(modifier = Modifier.weight(1f))
        ImageLoader(
            data = IconUtils.getWeatherIcon(context, dailyBean.iconDay),
            modifier = Modifier
                .padding(start = 7.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${dailyBean.tempMin}℃",
            modifier = Modifier
                .padding(start = 7.dp)
                .weight(1.4f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary
        )
        Text(
            text = "${dailyBean.tempMax}℃",
            modifier = Modifier
                .padding(start = 7.dp)
                .weight(1.4f),
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
