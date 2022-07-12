package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.zj.utils.weather.IconUtils

@Composable
fun WeatherAnimation(weather: String?) {
    val context = LocalContext.current
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            IconUtils.getWeatherAnimationIcon(context, weather)
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        modifier = Modifier.size(130.dp),
        progress = { progress }
    )
}

@Preview(showBackground = false, name = "天气动画")
@Composable
fun WeatherAnimationPreview() {
    WeatherAnimation("100")
}