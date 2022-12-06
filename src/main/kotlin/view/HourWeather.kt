package view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buildPainter
import model.weather.WeatherHourlyBean

@Composable
fun HourWeather(hourlyBeanList: List<WeatherHourlyBean.HourlyBean>?) {
    if (hourlyBeanList.isNullOrEmpty()) return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "24小时天气预报",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                items(hourlyBeanList) { hourlyBean ->
                    HourWeatherItem(hourlyBean)
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
private fun HourWeatherItem(hourlyBean: WeatherHourlyBean.HourlyBean) {
    Column(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hourlyBean.fxTime ?: "time",
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
        Image(painter = buildPainter("drawable/${hourlyBean.icon}.svg"), "", modifier = Modifier.padding(top = 7.dp))
        Text(
            text = "${hourlyBean.temp}℃",
            modifier = Modifier.padding(top = 7.dp),
            fontSize = 14.sp,
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview
@Composable
fun HourWeatherItemPreview() {
    val hourlyBean = WeatherHourlyBean.HourlyBean()
    hourlyBean.fxTime = "21时"
    hourlyBean.temp = "15"
    hourlyBean.icon = "100"
    HourWeatherItem(hourlyBean = hourlyBean)
}