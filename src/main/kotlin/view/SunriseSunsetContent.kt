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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.weather.WeatherDailyBean
import java.util.*
import kotlin.math.pow


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
                text = "太阳月亮",
                fontSize = 13.sp,
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
                moonrise = dailyBean.moonrise ?: "18:22",
                moonset = dailyBean.moonset ?: "07:22",
            )
            dailyBean.moonPhase
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
fun SunriseSunsetProgress(sunrise: String, sunset: String, moonrise: String, moonset: String) {
    val sunResult = getAccounted(sunrise, sunset)
    val moonResult = getAccounted(moonrise, moonset, false)

    // 这块的动画打开也看不到，效果其实挺好，先不要了，有需要可以打开试试
//    var showAnimate by remember { mutableStateOf(false) }
//    val result by animateFloatAsState(
//        if (showAnimate) results.toFloat() else 0f, animationSpec = tween(
//            durationMillis = 800,
//            delayMillis = 50,
//            easing = LinearOutSlowInEasing
//        )
//    )
    val sunImage = useResource("image/weather_sun.png", ::loadImageBitmap)
    val moonImage = useResource("image/weather_night.png", ::loadImageBitmap)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
//        showAnimate = true
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 10.dp)
        ) {

            val path = Path()
            path.moveTo(0f, size.height)
            // 三阶贝塞尔曲线
            path.quadraticBezierTo(
                size.width / 2,
                -size.height,
                size.width,
                size.height
            )

            drawPath(
                path = path, color = Color(red = 255, green = 193, blue = 7, alpha = 255),
                style = Stroke(width = 3f)
            )

            val sunX =
                (1.0 - sunResult).pow(2.0) * 0f + 2 * sunResult * (1 - sunResult) * (size.width / 2) + sunResult
                    .pow(2.0) * size.width

            val sunY =
                (1.0 - sunResult).pow(2.0) * size.height + 2 * sunResult * (1 - sunResult) * (-size.height) + sunResult
                    .pow(2.0) * size.height

            val moonX =
                (1.0 - moonResult).pow(2.0) * 0f + 2 * moonResult * (1 - moonResult) * (size.width / 2) + moonResult
                    .pow(2.0) * size.width

            val moonY =
                (1.0 - moonResult).pow(2.0) * size.height + 2 * moonResult * (1 - moonResult) * (-size.height) + moonResult
                    .pow(2.0) * size.height

            drawImage(
                image = sunImage,
                topLeft = Offset(
                    sunX.toFloat() - 24,
                    sunY.toFloat() - 24
                )
            )

            drawImage(
                image = moonImage,
                topLeft = Offset(
                    moonX.toFloat() - 24,
                    moonY.toFloat() - 24
                )
            )

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
                text = "日出：$sunrise\n" +
                        "月出：$moonrise",
                maxLines = 2,
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .wrapContentWidth(Alignment.End),
                text = "日落：$sunset\n" +
                        "月出：$moonset",
                maxLines = 2,
                fontSize = 12.sp,
            )
        }
    }

}

/**
 * 获取当前时间占白天时间的百分比（新增月亮）
 *
 * @param rise 日出或月出
 * @param set 日落或日落
 *
 * @return 百分比
 */
fun getAccounted(rise: String, set: String, isSun: Boolean = true): Double {
    val calendar = Calendar.getInstance()
    val currentMills = calendar.timeInMillis
    calendar.set(Calendar.HOUR_OF_DAY, getHour(rise))
    calendar.set(Calendar.MINUTE, getMinute(rise))
    val riseMills = calendar.timeInMillis
    if (!isSun) {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1)
    }
    calendar.set(Calendar.HOUR_OF_DAY, getHour(set))
    calendar.set(Calendar.MINUTE, getMinute(set))
    val setMills = calendar.timeInMillis
    val result = (currentMills - riseMills) / (setMills - riseMills).toDouble()
    return if (currentMills < riseMills) 0.0 else if (result > 1) 1.0 else result
}

fun getHour(sunrise: String): Int {
    return sunrise.substring(0, 2).toInt()
}

fun getMinute(sunrise: String): Int {
    return sunrise.substring(3, 5).toInt()
}