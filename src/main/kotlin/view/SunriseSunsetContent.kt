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
import java.awt.Point
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
    val result = getAccounted(sunrise, sunset)
    val image = useResource("image/weather_sun.png", ::loadImageBitmap)
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

            val controlPoints: ArrayList<Point> = arrayListOf()
            controlPoints.add(Point(0, size.height.toInt()))
            controlPoints.add(Point((size.width / 2).toInt(), (-size.height).toInt()))
            controlPoints.add(Point(size.width.toInt(), size.height.toInt()))
            drawImage(
                image = image,
                topLeft = Offset(
                    bezierX(controlPoints, 2, 0, result) - 12,
                    bezierY(controlPoints, 2, 0, result) - 12
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
 * 贝塞尔曲线递归算法, 本方法计算 X 轴坐标值
 * @param i 贝塞尔曲线阶数
 * @param j 贝塞尔曲线控制点
 * @param u 比例 / 时间 , 取值范围 0.0 ~ 1.0
 * @return
 */
private fun bezierX(controlPoints: ArrayList<Point>, i: Int, j: Int, u: Float): Float {
    return if (i == 1) {
        // 递归退出条件 : 贝塞尔曲线阶数 降为一阶
        // 一阶贝塞尔曲线点坐标 计算如下 :
        (1 - u) * controlPoints[j].y + u * controlPoints[j + 1].x
    } else (1 - u) * bezierX(controlPoints, i - 1, j, u) + u * bezierX(
        controlPoints,
        i - 1,
        j + 1,
        u
    )
}

/**
 * 贝塞尔曲线递归算法, 本方法计算 Y 轴坐标值
 * @param i 贝塞尔曲线阶数
 * @param j 贝塞尔曲线控制点
 * @param u 比例 / 时间 , 取值范围 0.0 ~ 1.0
 * @return
 */
private fun bezierY(controlPoints: ArrayList<Point>, i: Int, j: Int, u: Float): Float {
    return if (i == 1) {
        // 递归退出条件 : 贝塞尔曲线阶数 降为一阶
        (1 - u) * controlPoints[j].y + u * controlPoints[j + 1].y
    } else (1 - u) * bezierY(controlPoints, i - 1, j, u) + u * bezierY(
        controlPoints,
        i - 1,
        j + 1,
        u
    )
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