package com.zj.weather.ui.view.weather.widget

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.weather.R
import com.zj.weather.model.WeatherModel
import com.zj.weather.utils.weather.getSunriseSunsetContent
import com.zj.weather.utils.weather.getUvIndexDesc

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DayWeatherContent(
    weatherModel: WeatherModel?,
) {
    val context = LocalContext.current
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
            stringResource(id = R.string.uv_index_title),
            dailyBean?.uvIndex ?: "0",
            getUvIndexDesc(context, dailyBean?.uvIndex)
        )
        WeatherContentItem(
            modifier.fillMaxHeight(),
            stringResource(id = R.string.sun_title),
            getSunriseSunsetContent(
                context,
                dailyBean?.sunrise ?: "07:00",
                dailyBean?.sunset ?: "19:00"
            ),
            "${stringResource(id = R.string.sun_sunrise)}${dailyBean?.sunrise ?: "07:00"}\n${
                stringResource(
                    id = R.string.sun_sunset
                )
            }${dailyBean?.sunset ?: "19:00"}"
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
            if (nowBaseBean?.precip?.toFloat() ?: 0f > 0f)
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp, bottom = 10.dp)
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.air_pressure_title),
            "${nowBaseBean?.pressure ?: "0"}${stringResource(id = R.string.air_pressure_unit)}",
            stringResource(id = R.string.air_pressure_tip)
        )
        WeatherContentItem(
            modifier,
            stringResource(id = R.string.visibility_title),
            "${nowBaseBean?.vis ?: "0"}${stringResource(id = R.string.visibility_unit)}",
            stringResource(id = R.string.visibility_tip)
        )
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
                fontSize = 30.sp,
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
