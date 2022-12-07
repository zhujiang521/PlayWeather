// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import utils.buildPainter

@Composable
@Preview
fun App() {
    val appViewModel = AppViewModel()
    MaterialTheme {
        WeatherPage(appViewModel)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PlayWeather",
        state = rememberWindowState(width = 800.dp, height = 600.dp),
        icon = buildPainter("image/ic_launcher.svg")
    ) {
        App()
    }
}
