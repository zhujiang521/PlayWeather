package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.zui.animate.placeholder.placeholder

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
            stringResource(id = R.string.visibility_title),
            "${nowBaseBean?.vis ?: "0"}${stringResource(id = R.string.visibility_unit)}",
            stringResource(id = R.string.visibility_tip),
            weatherModel == null
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            stringResource(id = R.string.air_pressure_title),
            "${dailyBean?.pressure ?: "0"}${stringResource(id = R.string.air_pressure_unit)}",
            stringResource(id = R.string.air_pressure_tip),
            weatherModel == null
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherContentItem(
            Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            stringResource(id = R.string.body_temperature_title),
            "${nowBaseBean?.feelsLike ?: "0"}℃",
            stringResource(id = R.string.body_temperature_tip),
            weatherModel == null
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            stringResource(id = R.string.rainfall_title),
            "${nowBaseBean?.precip ?: "0"}${stringResource(id = R.string.rainfall_unit)}",
            if ((nowBaseBean?.precip?.toFloat()
                    ?: 0f) > 0f
            ) stringResource(id = R.string.rainfall_tip1)
            else stringResource(id = R.string.rainfall_tip2),
            weatherModel == null
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeatherContentItem(
            Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, end = 5.dp),
            stringResource(id = R.string.humidity_title),
            "${nowBaseBean?.humidity ?: "0"}%",
            stringResource(id = R.string.humidity_tip),
            weatherModel == null
        )
        WeatherContentItem(
            modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp, bottom = 5.dp, start = 5.dp),
            stringResource(id = R.string.wind_title),
            "${nowBaseBean?.windDir ?: "0"}${nowBaseBean?.windScale ?: ""}${stringResource(id = R.string.wind_unit)}",
            "${stringResource(id = R.string.wind_tip)}${nowBaseBean?.windSpeed ?: "0"}Km/H",
            weatherModel == null
        )
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
private fun WeatherContentItem(
    modifier: Modifier, title: String, value: String, tip: String, showPlaceholder: Boolean = false
) {
    Card(modifier = modifier, shape = RoundedCornerShape(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = title, fontSize = 11.sp, modifier = Modifier.placeholder(showPlaceholder))
            Text(
                text = value,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .placeholder(showPlaceholder),
                fontSize = 18.sp,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = tip,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .placeholder(showPlaceholder),
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
