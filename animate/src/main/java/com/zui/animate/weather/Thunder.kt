package com.zui.animate.weather

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zui.animate.R

@Composable
fun Thunder(
    modifier: Modifier = Modifier,
    durationMillis1: Int = 500,
    durationMillis2: Int = 1000
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val transition = rememberInfiniteTransition(label = "")
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis1),
            RepeatMode.Reverse
        ), label = ""
    )
    val alpha2 by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis2),
            RepeatMode.Reverse
        ), label = ""
    )
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.offset((screenWidthDp / 20).dp, 0.dp),
            painter = painterResource(id = R.drawable.ic_thunder1), contentDescription = "",
            alpha = alpha
        )
        Image(
            modifier = Modifier.align(Alignment.TopCenter),
            painter = painterResource(id = R.drawable.ic_thunder2), contentDescription = "",
            alpha = alpha
        )
        Image(
            modifier = Modifier.align(Alignment.TopEnd),
            painter = painterResource(id = R.drawable.ic_thunder3), contentDescription = "",
            alpha = alpha2
        )
        Image(
            modifier = Modifier.offset((screenWidthDp / 18).dp, 0.dp),
            painter = painterResource(id = R.drawable.ic_thunder4), contentDescription = "",
            alpha = alpha2
        )
    }
}