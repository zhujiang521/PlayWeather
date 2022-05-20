package com.zj.weather.view.tiger

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun NewYearWidget() {

    val isLand = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (!isLand) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TigerWidget()
            TextWidget()
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Red),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TigerWidget()
            TextWidget()
        }
    }

}

@Composable
fun TextWidget() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "恭喜", fontSize = 150.sp, color = Color.Yellow, fontFamily = FontFamily.Serif)
        Text(text = "发财", fontSize = 150.sp, color = Color.Yellow, fontFamily = FontFamily.Serif)
    }
}

@Preview(showBackground = false)
@Composable
fun TextWidgetPreview() {
    TextWidget()
}

@Preview(showBackground = false)
@Composable
fun NewYearPreview() {
    NewYearWidget()
}
