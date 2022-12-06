package view

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.WeatherModel

@Composable
fun DayWeatherContent(
    weatherModel: WeatherModel?,
) {
    val dailyBean = weatherModel?.dailyBean
    val nowBaseBean = weatherModel?.nowBaseBean
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
    ) {
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            "能见度",
            "${nowBaseBean?.vis ?: "0"}公里",
            "当前的能见度"
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            "气压",
            "${dailyBean?.pressure ?: "0"}百帕",
            "当前的大气压"
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherContentItem(
            Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            "体感温度",
            "${nowBaseBean?.feelsLike ?: "0"}℃",
            "与实际气温相似"
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            "降雨",
            "${nowBaseBean?.precip ?: "0"}毫米",
            if ((nowBaseBean?.precip?.toFloat() ?: 0f) > 0f)
                "今日有雨，记得带伞哦！"
            else "预计今日没雨"
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherContentItem(
            Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            "湿度",
            "${nowBaseBean?.humidity ?: "0"}%",
            "目前湿度正常"
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            "风",
            "${nowBaseBean?.windDir ?: "0"}${nowBaseBean?.windScale ?: ""}级",
            "当前风速为${nowBaseBean?.windSpeed ?: "0"}Km/H"
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
private fun WeatherContentItem(modifier: Modifier, title: String, value: String, tip: String) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = title, fontSize = 11.sp)
            Text(
                text = value,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = tip,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
fun WeatherContentItemPreview() {
    WeatherContentItem(Modifier, "模块标题", "值", "小标题提示")
}
