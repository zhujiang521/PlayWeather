package com.zj.weather.view.weather.widget

import android.content.Context
import android.graphics.Bitmap
import android.icu.util.Calendar
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
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.zj.model.weather.WeatherDailyBean
import com.zj.utils.XLog
import com.zj.weather.R
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun SunriseSunsetContent(dailyBean: WeatherDailyBean.DailyBean?) {
    if (dailyBean == null) {
        XLog.e("dailyBean is null")
        return
    }
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            Text(
                text = stringResource(id = R.string.sun_title),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                thickness = 0.4.dp
            )

            SunriseSunsetProgress(
                context,
                sunrise = dailyBean.sunrise,
                sunset = dailyBean.sunset,
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
fun SunriseSunsetProgress(context: Context, sunrise: String, sunset: String) {
    if (sunrise.isEmpty() || sunset.isEmpty()) {
        return
    }
    val result = getAccounted(sunrise, sunset)
    val bitmap = getBitmapFromVectorDrawable(context, R.drawable.x_sunny)
    val image = bitmap?.asImageBitmap()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(horizontal = 10.dp)
        ) {


            clipRect(0f, 0f, size.width, size.height/2, ClipOp.Intersect) {
                drawCircle(
                    color = Color(red = 255, green = 193, blue = 7, alpha = 255),
                    radius = size.width / 2,
                    center = Offset(size.width / 2, size.height),
                    style = Stroke(
                        width = 3f,
                    )
                )
            }

            val mRadius = size.width / 2
            val x = mRadius - (mRadius * cos((180 * result) * Math.PI / 180)) - 40
            val y = mRadius - (mRadius * sin((180 * result) * Math.PI / 180)) - 30
            if (image != null) {
                drawImage(
                    image = image,
                    topLeft = Offset(x.toFloat(), y.toFloat())
                )
            }

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
                text = "${stringResource(id = R.string.sun_sunrise)}$sunrise",
                fontSize = 12.sp,
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .wrapContentWidth(Alignment.End),
                text = "${stringResource(id = R.string.sun_sunset)}$sunset",
                fontSize = 12.sp,
            )
        }
    }

}

/**
 * SVG 转 Bitmap
 * @param context 上下文
 * @param drawableId SVG资源
 * @return
 */
fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context!!, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = android.graphics.Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
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
    XLog.w("currentMinutes:$currentMinutes   sunriseMinutes:$sunriseMinutes   sunsetMinutes:$sunsetMinutes   accounted:$accounted")
    val result = if (accounted > 1) {
        1f
    } else if (accounted < 0) {
        0f
    } else {
        accounted
    }
    XLog.w("result:$result")
    return result
}

/**
 * 时间转分钟数
 *
 * @param sunrise 时间，格式为"14:22"
 *
 * @return 总分钟数
 */
fun getMinutes(sunrise: String?): Int {
    val s = if (sunrise == null || sunrise.isEmpty()) {
        "00:00"
    } else sunrise
    val hour = s.substring(0, 2).toInt()
    val minutes = s.substring(3, 5).toInt()
    val total = hour * 60 + minutes
    XLog.w("hour:$hour   minutes:$minutes   total:$total")
    return total
}
