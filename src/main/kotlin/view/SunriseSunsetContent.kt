package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.weather.WeatherDailyBean
import java.util.*


@Composable
fun SunriseSunsetContent(dailyBean: WeatherDailyBean.DailyBean?) {
    if (dailyBean == null) {
        return
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = "日出日落",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                thickness = 0.4.dp
            )

            SunriseSunsetProgress(
                sunrise = dailyBean.sunrise ?: "07:22",
                sunset = dailyBean.sunset ?: "18:22",
            )
        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}


/**
 * 日出日落图
 *
 * @param sunrise 日出时间
 * @param sunset 日落时间
 */
@Composable
fun SunriseSunsetProgress(sunrise: String, sunset: String) {
//    val result = getAccounted(sunrise, sunset)
//    val bitmap = getBitmapFromVectorDrawable(context, R.drawable.x_sunny)
//    val image = bitmap?.asImageBitmap()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 10.dp)
        ) {
            val path = Path()
            path.moveTo(0f, size.height)
            // 三阶贝塞尔曲线
            path.cubicTo(
                0f,
                size.height,
                size.width / 2,
                -size.height,
                size.width,
                size.height
            )

            drawPath(
                path = path, color = Color(red = 255, green = 193, blue = 7, alpha = 255),
                style = Stroke(width = 3f)
            )


//            val evaluate = evaluate(
//                result,
//                floatArrayOf(0f, size.height),
//                floatArrayOf(size.width / 4, -size.height / 2),
//                floatArrayOf(size.width / 4 * 3, 0f),
//                floatArrayOf(size.width, size.height)
//            )
//            drawImage(
//                image = useResource("drawable/100.svg", ::loadImageBitmap),
//                topLeft = Offset(
//                    evaluate[0] - 45,
//                    evaluate[1] - 45
//                )
//            )

            drawPoints(
                points = arrayListOf(
                    Offset(0f, size.height),
                    Offset(size.width, size.height)
                ),
                pointMode = PointMode.Points,
                color = Color(red = 255, green = 193, blue = 7, alpha = 255),
                strokeWidth = 20f,
                cap = StrokeCap.Round,
            )

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(
                modifier = Modifier
                    .wrapContentWidth(Alignment.Start),
                text = "日出：$sunrise",
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .wrapContentWidth(Alignment.End),
                text = "日落：$sunset",
                fontSize = 12.sp,
            )
        }
    }

}


/**
 *
 * @param fraction 变量
 * @param point0 贝塞尔曲线起点
 * @param point3 贝塞尔曲线终点
 * @return 因为需要的点是从下到上....所以p0,p1,p2,p3的点是从下打上的
 */
fun evaluate(
    fraction: Float,
    point0: FloatArray,
    point1: FloatArray,
    point2: FloatArray,
    point3: FloatArray
): FloatArray {
    val currentPosition = FloatArray(2)
    //贝塞尔公式计算X点
    currentPosition[0] =
        point0[0] * (1 - fraction) * (1 - fraction) * (1 - fraction) + point1[0] * 3 * fraction * (
                1 - fraction) * (1 - fraction) + point2[0] * 3 * (1 - fraction) * fraction * fraction + point3[0] * fraction * fraction * fraction
    //贝塞尔公式计算Y点
    currentPosition[1] =
        point0[1] * (1 - fraction) * (1 - fraction) * (1 - fraction) + point1[1] * 3 * fraction * (1 - fraction) * (1 - fraction) + point2[1] * 3 * (
                1 - fraction) * fraction * fraction + point3[1] * fraction * fraction * fraction
    return currentPosition
}

/**
 * 获取当前时间占白天时间的百分比
 *
 * @param sunrise 日出
 * @param sunset 日落
 *
 * @return 百分比
 */
private fun getAccounted(sunrise: String, sunset: String): Float {
    val calendar = Calendar.getInstance()
    val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    val sunriseMinutes = getMinutes(sunrise)
    val sunsetMinutes = getMinutes(sunset)
    val accounted =
        (currentMinutes.toFloat() - sunriseMinutes.toFloat()) / (sunsetMinutes.toFloat() - sunriseMinutes.toFloat())
    val result = if (accounted > 1) {
        1f
    } else if (accounted < 0) {
        0f
    } else {
        accounted
    }
    return result
}

/**
 * 时间转分钟数
 *
 * @param sunrise 时间，格式为"14:22"
 *
 * @return 总分钟数
 */
fun getMinutes(sunrise: String): Int {
    val hour = sunrise.substring(0, 2).toInt()
    val minutes = sunrise.substring(3, 5).toInt()
    return hour * 60 + minutes
}