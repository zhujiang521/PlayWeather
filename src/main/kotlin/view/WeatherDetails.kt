package view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.getWeatherIcon
import model.weather.WeatherNowBean
import utils.getTimeNameForObs

@Composable
fun WeatherDetails(
    modifier: Modifier,
    nowBaseBean: WeatherNowBean.NowBaseBean?,
    onAddClick: () -> Unit,
    onRefreshClick: () -> Unit
) {

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onAddClick) {
                Icon(Icons.Sharp.Add, "")
            }
            Text(nowBaseBean?.city ?: "北京", fontSize = 14.sp)
            Spacer(modifier = Modifier.weight(1f))

            Text(nowBaseBean?.obsTime?.getTimeNameForObs() ?: "现在", fontSize = 14.sp)
            IconButton(onClick = onRefreshClick) {
                Icon(Icons.Sharp.Refresh, "")
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            RotateWeatherIcon(nowBaseBean?.icon ?: "100")

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text("${nowBaseBean?.temp ?: "0"}°", fontSize = 80.sp, maxLines = 1)
                Text(nowBaseBean?.text ?: "晴", fontSize = 15.sp, maxLines = 1)
            }

        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WeatherItem(nowBaseBean?.windDir ?: "南风", "${nowBaseBean?.windScale ?: "1"}级")
            WeatherItem("降雨", "${nowBaseBean?.precip ?: "0"}毫米")
            WeatherItem("能见度", "${nowBaseBean?.vis ?: "0"}公里")
            WeatherItem("气压", "${nowBaseBean?.pressure ?: "0"}百帕")
        }

    }

}

@Composable
fun WeatherItem(name: String, value: String) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(name, fontSize = 14.sp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(5.dp))
        Text(value, fontSize = 14.sp, color = Color.DarkGray)
    }
}

/**
 * 动画图标
 *
 * 这块添加了无限重复动画，如果是晴天，也就是小太阳则旋转，如果不是则进行左右平移操作
 */
@Composable
private fun RotateWeatherIcon(icon: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val modifier = if (icon == "100") {
        val rotate by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(3500, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Modifier.rotate(rotate)
    } else {
        val offsetX by infiniteTransition.animateValue(
            initialValue = (-30).dp, // 初始值
            targetValue = 30.dp, // 目标值
            typeConverter = TwoWayConverter(
                { AnimationVector1D(it.value) },
                { it.value.dp }), // 类型转换
            animationSpec = infiniteRepeatable(  // 动画规格!!!
                animation = tween(3500, easing = LinearOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
        Modifier.offset(x = offsetX)
    }
    Image(
        painter = painterResource(getWeatherIcon(icon)),
        "",
        modifier = modifier.size(170.dp).padding(10.dp)
    )
}