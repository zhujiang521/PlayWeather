package com.zj.weather.ui.view.weather

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.room.entity.CityInfo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HeaderWeather(
    fontSize: TextUnit,
    cityListClick: () -> Unit,
    cityInfo: CityInfo,
    weatherNow: WeatherNowBean.NowBaseBean?,
    isLand: Boolean = false
) {
    AnimatedVisibility(visible = fontSize.value > 40f || isLand) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = cityListClick
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add"
                    )
                }
            }

            Text(
                text = cityInfo.name,
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 30.sp,
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "${weatherNow?.text}  ${weatherNow?.temp}℃",
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                fontSize = if (isLand) 45.sp else fontSize,
                color = MaterialTheme.colors.primary
            )

        }
    }
}