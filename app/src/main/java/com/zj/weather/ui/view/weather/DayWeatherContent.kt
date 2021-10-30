package com.zj.weather.ui.view.weather

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.weather.WeatherDailyBean
import com.qweather.sdk.bean.weather.WeatherNowBean

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayWeatherContent(weatherNow: WeatherNowBean.NowBaseBean?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
            .alpha(0.9f)
        WeatherContentItem(modifier, "体感温度", "${weatherNow?.feelsLike}℃", "与实际气温相似")
        WeatherContentItem(modifier, "降雨", "${weatherNow?.precip}毫米", "预计啥时候会有雨")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 5.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
            .alpha(0.9f)
        WeatherContentItem(modifier, "湿度", "${weatherNow?.humidity}%", "湿度非常好")
        WeatherContentItem(
            modifier, "风", "${weatherNow?.windDir}${weatherNow?.windScale}级",
            "当前风速为${weatherNow?.windSpeed}公里/小时"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
            .alpha(0.9f)
        WeatherContentItem(modifier, "气压", "${weatherNow?.pressure}百帕", "当前的大气压")
        WeatherContentItem(modifier, "能见度", "${weatherNow?.vis}公里", "当前的能见度")
    }

}

@Composable
private fun WeatherContentItem(modifier: Modifier, title: String, value: String, tip: String) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = title, fontSize = 14.sp)
            Text(
                text = value,
                modifier = Modifier.padding(top = 10.dp),
                fontSize = 34.sp,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = tip,
                modifier = Modifier.padding(top = 15.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview(showBackground = false, name = "具体天气模块item")
@Composable
fun WeatherContentItemPreview() {
    WeatherContentItem(Modifier, "模块标题", "值", "小标题提示")
}
