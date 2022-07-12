package com.zj.utils.lce

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.zj.utils.R

@Composable
fun NoContent(tip: String = "当前无内容") {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.weather_no_data)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(130.dp),
            progress = { progress }
        )
        Text(text = tip, modifier = Modifier.padding(10.dp))
    }
}