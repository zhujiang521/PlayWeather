package com.zj.weather.ui.view.weather

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.qweather.sdk.bean.weather.WeatherNowBean
import com.zj.weather.room.entity.CityInfo

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShrinkHeaderHeather(
    fontSize: TextUnit,
    cityInfo: CityInfo,
    cityListClick: () -> Unit,
    weatherNow: WeatherNowBean.NowBaseBean?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        AnimatedVisibility(
            visible = fontSize.value < 40f,
            enter = fadeIn() + expandVertically(animationSpec = tween(300)),
            exit = shrinkVertically(animationSpec = tween(300)) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        modifier = Modifier
                            .wrapContentWidth(Alignment.Start), onClick = {}
                    ) {}
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentWidth(Alignment.CenterHorizontally),
                        text = cityInfo.name,
                        maxLines = 1,
                        fontSize = 30.sp,
                        color = MaterialTheme.colors.primary,
                        overflow = TextOverflow.Ellipsis,
                    )
                    IconButton(
                        modifier = Modifier.wrapContentWidth(Alignment.End),
                        onClick = cityListClick
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "add"
                        )
                    }
                }

                Text(
                    text = "${weatherNow?.text}  ${weatherNow?.temp}℃",
                    fontSize = 20.sp,
                    color = MaterialTheme.colors.primary
                )
            }
        }
    }
}