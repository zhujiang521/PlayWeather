package com.zj.weather.view.season

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.zj.utils.XLog
import com.zj.weather.R

/**
 * 季节页面，点击天气页面进入
 */
@Composable
fun SeasonPage() {
    val season = getSeason()
    XLog.i("season:$season")
    when (season) {
        Season.SPRING -> {
            Season(R.mipmap.back_100d, season.name)
        }
        Season.SUMMER -> {
            Season(R.mipmap.back_101d, season.name)
        }
        Season.FALL -> {
            Season(R.mipmap.back_104d, season.name)
        }
        Season.WINTER -> {
            Season(R.mipmap.back_300d, season.name)
        }
    }

}


@Composable
private fun Season(back: Int, name: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = back),
            contentScale = ContentScale.FillBounds,
            contentDescription = ""
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = name)
        }
    }
}


