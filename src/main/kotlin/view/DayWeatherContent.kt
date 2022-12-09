package view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import model.WeatherModel
import model.weather.DayItemData

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
        val modifier = Modifier
            .weight(1f)
            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
        WeatherContentItem(
            modifier = modifier,
            DayItemData(
                "能见度",
                "${nowBaseBean?.vis ?: "0"}公里",
                "当前的能见度",
                "关于能见度",
                "能见度会告诉你可以清晰地看到多远以外的物体，如建筑和山丘等。能见度测量大气透明度，不考虑光照强度或障碍物。能见度10公里及以上为良好。"
            )
        )
        WeatherContentItem(
            modifier = modifier, DayItemData(
                "气压",
                "${dailyBean?.pressure ?: "0"}百帕",
                "当前的大气压", "关于气压", "气压的显著急剧变化可用于预测天气变化。例如，" +
                        "气压降低可能表示兩雪即将来临，则可能表示天气将要好转。气压也被称为大气压或大气压强。"
            )

        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
        WeatherContentItem(
            modifier, DayItemData(
                "体感温度",
                "${nowBaseBean?.feelsLike ?: "0"}°",
                "与实际气温相似", "关于体感温度", "体感温度传达身体感觉有多暖或多冷，可能与实际温度不同。体感温度受湿度和风影响。"
            )

        )
        WeatherContentItem(
            modifier = modifier, DayItemData(
                "降雨",
                "${nowBaseBean?.precip ?: "0"}毫米",
                if ((nowBaseBean?.precip?.toFloat() ?: 0f) > 0f)
                    "今日有雨，记得带伞哦！"
                else "预计今日没雨", "降水强度", "未来的一个降水信息，如果由降水要注意带伞，也要注意保暖"
            )

        )
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        val modifier = Modifier
            .weight(1f)
            .padding(top = 5.dp, bottom = 5.dp, end = 5.dp)
        WeatherContentItem(
            modifier, DayItemData(
                "湿度",
                "${nowBaseBean?.humidity ?: "0"}%",
                "目前湿度正常", "关于相对湿度", "相对湿度，一般简称为湿度，是空气中含水量与空气可容纳水量的比值。" +
                        "气温越高，空气可容纳的水量就越多。若相对湿度接近100%，则意味着可能结露或起雾。"
            )
        )
        WeatherContentItem(
            modifier = modifier, DayItemData(
                "风",
                "${nowBaseBean?.windDir ?: "南风"}${nowBaseBean?.windScale ?: ""}级",
                "当前风速为${nowBaseBean?.windSpeed ?: "0"}Km/H", "关于风速和阵风", "风速的计算取短时间内的平均值。" +
                        "阵风是高于此平均值的短暂强风。阵风的持续时间通常低于 20 秒。"
            )

        )
    }
    Spacer(modifier = Modifier.height(5.dp))
}

@Composable
private fun WeatherContentItem(modifier: Modifier, data: DayItemData) {
    var showPopupWindow by remember { mutableStateOf(false) }
    Card(modifier = modifier.clickable {
        showPopupWindow = true
    }, shape = RoundedCornerShape(10.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = data.title, fontSize = 11.sp)
            Text(
                text = data.value,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 18.sp,
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = data.tip,
                modifier = Modifier.padding(top = 8.dp),
                fontSize = 13.sp,
                color = MaterialTheme.colors.onSecondary
            )
        }
    }

    CursorDropdownMenu(
        showPopupWindow,
        onDismissRequest = { showPopupWindow = false },
        modifier = modifier.width(300.dp).padding(horizontal = 15.dp).padding(bottom = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = data.titleDetails,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary
            )
            IconButton(onClick = { showPopupWindow = false }) {
                Icon(Icons.Sharp.Close, "Close")
            }
        }
        Text(
            text = data.valueDetails,
            fontSize = 13.sp,
        )
    }
}