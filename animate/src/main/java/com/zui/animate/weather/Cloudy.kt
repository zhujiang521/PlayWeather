package com.zui.animate.weather

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zui.animate.R

@Composable
fun Cloudy(modifier: Modifier = Modifier, durationMillis: Int = 20000) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val transition = rememberInfiniteTransition(label = "")
    val rotate by transition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = LinearEasing),
            RepeatMode.Reverse
        ), label = ""
    )
    val offset by transition.animateFloat(
        initialValue = 0f,
        targetValue = screenWidthDp.toFloat(),
        animationSpec = infiniteRepeatable(
            tween(durationMillis, easing = LinearEasing),
            RepeatMode.Reverse
        ), label = ""
    )
    Box(modifier = modifier) {
        Image(
            modifier = Modifier
                .rotate(rotate),
            painter = painterResource(id = R.drawable.ic_yun1), contentDescription = ""
        )
        Image(
            modifier = Modifier
                .offset(offset.dp),
            painter = painterResource(id = R.drawable.ic_yun2), contentDescription = ""
        )
    }
}