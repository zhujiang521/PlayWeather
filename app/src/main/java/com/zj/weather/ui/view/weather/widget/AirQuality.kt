package com.zj.weather.ui.view.weather.widget

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
import com.qweather.sdk.bean.air.AirNowBean
import com.zj.weather.R


@Composable
fun AirQuality(airNowBean: AirNowBean.NowBean?) {
    if (airNowBean == null) return
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            ,
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
                fontSize = 24.sp,
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
}

/**
 * 空气质量图
 * 0-50	一级	优	绿色
 * 51-100	二级	良	黄色
 * 101-150	三级	轻度污染	橙色
 * 151-200	四级	中度污染	红色
 * 201-300	五级	重度污染	紫色
 * >300	六级	严重污染	褐红色
 *
 * @param aqi 空气质量数值
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

@Preview(showBackground = false, name = "空气质量item")
@Composable
fun AirQualityProgressPreview() {
    AirQualityProgress(aqi = 400)
}
