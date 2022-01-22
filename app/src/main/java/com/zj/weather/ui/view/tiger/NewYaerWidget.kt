package com.zj.weather.ui.view.tiger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

@Composable
fun NewYearWidget() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TigerWidget()
        TextWidget()
    }

}

@Composable
fun TextWidget() {
    Text(text = "恭喜", fontSize = 150.sp, color = Color.Yellow, fontFamily = FontFamily.Serif)
    Text(text = "发财", fontSize = 150.sp, color = Color.Yellow, fontFamily = FontFamily.Serif)
}
