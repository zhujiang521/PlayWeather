package view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buildPainter
import model.weather.WeatherNowBean

@Composable
fun TodayWeather(nowBaseBean: WeatherNowBean.NowBaseBean?) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "当前天气", fontSize = 14.sp, modifier = Modifier
                    .padding(bottom = 7.dp)
            )
            Divider(thickness = 0.4.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RotateWeatherIcon(nowBaseBean?.icon ?: "100")
                Text(
                    modifier = Modifier.width(200.dp),
                    text = "北京",
                    fontSize = 35.sp,
                    color = MaterialTheme.colors.primary,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${nowBaseBean?.text ?: "晴"}  ${nowBaseBean?.temp ?: "0"}℃",
                    fontSize = 35.sp,
                    color = MaterialTheme.colors.primary
                )
            }

        }
    }
    Spacer(modifier = Modifier.height(10.dp))
}

/**
 * 动画图标
 */
/**
 * 动画图标
 */
@Composable
private fun RotateWeatherIcon(icon: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotate by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3500, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Image(
        painter = buildPainter("drawable/$icon.svg"),
        "",
        modifier = Modifier.size(100.dp).padding(10.dp).rotate(rotate)
    )
}