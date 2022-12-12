package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.weather.WeatherDailyBean
import utils.getWeatherIcon

@Composable
fun WeekWeather(dayBeanList: List<WeatherDailyBean.DailyBean>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "7日天气预报",
                fontSize = 13.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            if (dayBeanList.isNullOrEmpty()) {
                WeekDayWeatherItem(WeatherDailyBean.DailyBean())
            } else {
                dayBeanList.forEach { dailyBean ->
                    Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
                    WeekDayWeatherItem(dailyBean)
                }
            }
        }
    }
}

@Composable
private fun WeekDayWeatherItem(dailyBean: WeatherDailyBean.DailyBean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = dailyBean.fxDate ?: "今天",
            modifier = Modifier
                .weight(1f)
                .padding(start = 3.dp),
            fontSize = 15.sp,
            color = MaterialTheme.colors.onSecondary
        )
        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(getWeatherIcon(dailyBean.iconDay)),
            "",
            modifier = Modifier.size(25.dp)
        )

        Spacer(modifier = Modifier.weight(1.2f))
        Text(
            text = "${dailyBean.tempMin ?: "0"}°",
            modifier = Modifier.width(50.dp)
                .padding(end = 15.dp),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onSecondary
        )
        TemperatureChart(
            Modifier.weight(2f),
            dailyBean.weekMin,
            dailyBean.weekMax,
            dailyBean.tempMin?.toInt() ?: -20,
            dailyBean.tempMax?.toInt() ?: 40,
            dailyBean.temp
        )
        Text(
            text = "${dailyBean.tempMax ?: "0"}°",
            modifier = Modifier
                .width(55.dp)
                .padding(start = 15.dp, end = 5.dp),
            fontSize = 15.sp,
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.onSecondary
        )
    }
}

/**
 * 气温条状图
 *
 * @param min 未来几天最低温度
 * @param max 未来几天最高温度
 * @param currentMin 这一天的最低温度
 * @param currentMax 这一天的最高温度
 * @param currentTemperature 当天当前温度
 */
@Composable
private fun TemperatureChart(
    modifier: Modifier,
    min: Int,
    max: Int,
    currentMin: Int,
    currentMax: Int,
    currentTemperature: Int = -1
) {
    val currentMinColor: Color = getTemperatureColor(currentMin)
    val currentMaxColor: Color = getTemperatureColor(currentMax)
    // 计算周温差
    val num = max - min
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        // 绘制底条
        drawLine(
            color = Color.Gray,
            start = Offset.Zero,
            end = Offset(size.width, 0f),
            strokeWidth = 10f,
            cap = StrokeCap.Round,
        )
        // 绘制这一天的气温
        drawLine(
            brush = Brush.linearGradient(
                0.0f to currentMinColor,
                1.0f to currentMaxColor,
            ),
            start = Offset(size.width / num * (currentMin - min), 0f),
            end = Offset(size.width / num * (currentMax - min), 0f),
            strokeWidth = 10f,
            cap = StrokeCap.Round,
        )
        // 如果是当天，则绘制当前温度
        if (currentTemperature > -100) {
            drawPoints(
                points = arrayListOf(
                    Offset(size.width / num * (currentTemperature - min), 0f)
                ),
                pointMode = PointMode.Points,
                color = Color.White,
                strokeWidth = 10f,
                cap = StrokeCap.Round,
            )
        }
    }
}

/**
 * 获取不同气温的颜色值，需要动态判断
 */
private fun getTemperatureColor(temperature: Int): Color {
    return if (temperature < -20) {
        Color(red = 26, green = 92, blue = 249)
    } else if (temperature < -15) {
        Color(red = 16, green = 103, blue = 255)
    } else if (temperature < -10) {
        Color(red = 28, green = 122, blue = 254)
    } else if (temperature < -5) {
        Color(red = 52, green = 151, blue = 229)
    } else if (temperature < 0) {
        Color(red = 65, green = 174, blue = 250)
    } else if (temperature < 5) {
        Color(red = 86, green = 201, blue = 205)
    } else if (temperature < 10) {
        Color(red = 86, green = 203, blue = 299)
    } else if (temperature < 15) {
        Color(red = 151, green = 201, blue = 142)
    } else if (temperature < 20) {
        Color(red = 247, green = 196, blue = 34)
    } else if (temperature < 25) {
        Color(red = 209, green = 123, blue = 11)
    } else if (temperature < 30) {
        Color(red = 253, green = 138, blue = 11)
    } else {
        Color(red = 248, green = 60, blue = 30)
    }
}
