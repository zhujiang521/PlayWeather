package view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buildPainter
import model.weather.WeatherDailyBean
import utils.getDateWeekName

@Composable
fun DayWeather(dayBeanList: List<WeatherDailyBean.DailyBean>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "7日天气预报",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            dayBeanList?.forEach { dailyBean ->
                Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
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
            text = dailyBean.fxDate?.getDateWeekName() ?: "date",
            modifier = Modifier
                .weight(1f)
                .padding(start = 3.dp),
            fontSize = 15.sp,
            color = MaterialTheme.colors.onSecondary
        )
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = buildPainter("drawable/${dailyBean.iconDay}.svg"),
            "",
            modifier = Modifier.padding(top = 7.dp)
        )

        Spacer(modifier = Modifier.weight(1.2f))
        Text(
            text = "${dailyBean.tempMin ?: "0"}°",
            modifier = Modifier
                .padding(start = 7.dp)
                .weight(2f),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSecondary
        )
        Text(
            text = "${dailyBean.tempMax ?: "0"}°",
            modifier = Modifier
                .padding(start = 7.dp, end = 3.dp)
                .weight(2f),
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Preview
@Composable
fun DayWeatherItemPreview() {
    val dailyBean = WeatherDailyBean.DailyBean()
    dailyBean.fxDate = "周一"
    dailyBean.iconDay = "100"
    dailyBean.tempMin = "10"
    dailyBean.tempMax = "20"
    DayWeatherItem(dailyBean = dailyBean)
}
