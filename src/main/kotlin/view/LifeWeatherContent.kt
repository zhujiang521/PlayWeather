package view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import buildPainter
import model.indices.WeatherLifeIndicesBean


@Composable
fun LifeWeatherContent(weatherLifeList: List<WeatherLifeIndicesBean.WeatherLifeIndicesItem>?) {
    if (weatherLifeList.isNullOrEmpty()) return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "生活指数",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 7.dp, start = 10.dp, end = 10.dp)
            )
            Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.4.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            ) {
                val modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
                WeatherLifeItem(
                    modifier, "drawable/100.svg", "运动", weatherLifeList[0].category
                )
                WeatherLifeItem(
                    modifier, "drawable/101.svg", "洗车", weatherLifeList[1].category
                )
                WeatherLifeItem(
                    modifier,
                    "drawable/102.svg",
                    "穿衣",
                    weatherLifeList[2].category
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 5.dp, end = 5.dp)
            ) {
                val modifier = Modifier
                    .weight(1f)
                    .padding(5.dp)
                WeatherLifeItem(
                    modifier, "drawable/103.svg", "紫外线", weatherLifeList[3].category
                )
                WeatherLifeItem(
                    modifier,
                    "drawable/104.svg",
                    "旅游",
                    weatherLifeList[4].category
                )
                WeatherLifeItem(
                    modifier, "drawable/150.svg", "感冒", weatherLifeList[5].category
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
        }
    }
}

@Composable
fun WeatherLifeItem(modifier: Modifier, imgRes: String, title: String, value: String?) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(painter = buildPainter(imgRes), contentDescription = "", modifier = Modifier.size(30.dp))

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = title, fontSize = 12.sp)
            Text(
                text = value ?: "",
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 14.sp,
                color = MaterialTheme.colors.primary
            )
        }

    }
}
