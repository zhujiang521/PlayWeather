package com.zj.utils.lce

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.zj.utils.R

@Composable
fun LoadingContent(
    alertDialog: MutableState<Boolean>,
) {
    if (!alertDialog.value) return
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.weather_load)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Dialog(onDismissRequest = {
        alertDialog.value = false
    }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress }
            )
        }
    }
}
