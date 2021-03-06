package com.zj.weather.view.weather.widget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zj.model.air.AirNowBean
import com.zj.weather.R


@Composable
fun AirQuality(airNowBean: AirNowBean.NowBean?) {
    if (airNowBean == null) return
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.air_quality_title), fontSize = 14.sp)
            Text(
                text = "${airNowBean.aqi ?: "10"} - ${
                    airNowBean.category ?: stringResource(id = R.string.air_quality_level)
                }",
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 20.sp,
                color = MaterialTheme.colors.primary
            )
            Text(
                text = "${
                    stringResource(id = R.string.air_quality_Current_aqi)
                }${airNowBean.aqi ?: "10"}${airNowBean.primary ?: ""}",
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            AirQualityProgress((airNowBean.aqi ?: "10").toInt())
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

/**
 * ???????????????
 * 0-50	??????	???	??????
 * 51-100	??????	???	??????
 * 101-150	??????	????????????	??????
 * 151-200	??????	????????????	??????
 * 201-300	??????	????????????	??????
 * >300	??????	????????????	?????????
 *
 * @param aqi ??????????????????
 */
@Composable
fun AirQualityProgress(aqi: Int) {
    val aqiValue = if (aqi < 500) {
        aqi
    } else {
        500
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        drawLine(
            brush = Brush.linearGradient(
                0.0f to Color(red = 139, green = 195, blue = 74),
                0.1f to Color(red = 255, green = 239, blue = 59),
                0.2f to Color(red = 255, green = 152, blue = 0),
                0.3f to Color(red = 244, green = 67, blue = 54),
                0.4f to Color(red = 156, green = 39, blue = 176),
                1.0f to Color(red = 143, green = 0, blue = 0),
            ),
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            strokeWidth = 20f,
            cap = StrokeCap.Round,
        )
        drawPoints(
            points = arrayListOf(
                Offset(size.width / 500 * aqiValue, 0f)
            ),
            pointMode = PointMode.Points,
            color = Color.White,
            strokeWidth = 20f,
            cap = StrokeCap.Round,
        )
    }
}

@Preview(showBackground = false, name = "????????????item")
@Composable
fun AirQualityProgressPreview() {
    AirQualityProgress(aqi = 400)
}
