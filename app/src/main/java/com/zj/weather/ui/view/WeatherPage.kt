package com.zj.weather.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.zj.weather.MainViewModel
import com.zj.weather.R
import com.zj.weather.ui.view.weather.DayWeather
import com.zj.weather.ui.view.weather.DayWeatherContent
import com.zj.weather.ui.view.weather.HourWeather
import com.zj.weather.utils.IconUtils
import com.zj.weather.utils.ImageLoader

@Composable
fun WeatherPage(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    val weatherNow by mainViewModel.weatherNow.observeAsState()
    val hourlyBeanList by mainViewModel.hourlyBeanList.observeAsState(listOf())
    val dayBeanList by mainViewModel.dayBeanList.observeAsState(listOf())
    val scrollState = rememberScrollState()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            R.raw.weather_foggy
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        speed = 2f,
        iterations = LottieConstants.IterateForever
    )
    Box(modifier = Modifier.fillMaxSize()) {
        ImageLoader(
            modifier = Modifier.fillMaxSize(),
            data = IconUtils.getWeatherBack(context,weatherNow?.icon)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // 权限申请
//            FeatureThatRequiresCameraPermissions {
//                startSettingAppPermission(context)
//            }

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    modifier = Modifier
                        .wrapContentWidth(Alignment.Start), onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "add"
                    )
                }
            }

            Text(
                text = "北京市",
                modifier = Modifier.padding(top = 5.dp),
                fontSize = 30.sp,
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "${weatherNow?.text}  ${weatherNow?.temp}℃",
                modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                fontSize = 40.sp,
                color = MaterialTheme.colors.primary
            )

            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(130.dp),
                progress = progress
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 未来24小时天气预报
            HourWeather(hourlyBeanList)

            Spacer(modifier = Modifier.height(10.dp))

            // 未来7天天气预报
            DayWeather(dayBeanList)

            // 当天具体天气数值
            DayWeatherContent(weatherNow)
        }

    }
}
