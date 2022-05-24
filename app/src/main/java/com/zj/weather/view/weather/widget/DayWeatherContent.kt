package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.WeatherModel
import com.zj.weather.R

@Composable
fun DayWeatherContent(
    weatherModel: WeatherModel?,
) {
    val dailyBean = weatherModel?.dailyBean
    val nowBaseBean = weatherModel?.nowBaseBean
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.visibility_title),
            "${nowBaseBean?.vis ?: "0"}${stringResource(id = R.string.visibility_unit)}",
            stringResource(id = R.string.visibility_tip)
        )
        WeatherContentItem(
            modifier.fillMaxHeight(),
            stringResource(id = R.string.sun_title),
            "${stringResource(id = R.string.sun_sunrise)}${dailyBean?.sunrise ?: "07:00"}",
            "${stringResource(id = R.string.sun_sunset)}${dailyBean?.sunset ?: "19:00"}"
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.body_temperature_title),
            "${nowBaseBean?.feelsLike ?: "0"}℃",
            stringResource(id = R.string.body_temperature_tip)
        )
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.rainfall_title),
            "${nowBaseBean?.precip ?: "0"}${stringResource(id = R.string.rainfall_unit)}",
            if ((nowBaseBean?.precip?.toFloat() ?: 0f) > 0f)
                stringResource(id = R.string.rainfall_tip1)
            else stringResource(id = R.string.rainfall_tip2)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.humidity_title),
            "${nowBaseBean?.humidity ?: "0"}%",
            stringResource(id = R.string.humidity_tip)
        )
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.wind_title),
            "${nowBaseBean?.windDir ?: "0"}${nowBaseBean?.windScale ?: ""}${stringResource(id = R.string.wind_unit)}",
            "${stringResource(id = R.string.wind_tip)}${nowBaseBean?.windSpeed ?: "0"}Km/H"
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
            Text(text = title, fontSize = 13.sp)
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

@Preview(showBackground = false, name = "具体天气模块item")
@Composable
fun WeatherContentItemPreview() {
    WeatherContentItem(Modifier, "模块标题", "值", "小标题提示")
}
