// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import view.BuildTray
import view.MenuBarWeather


@Composable
@Preview
fun App() {
    val appViewModel = AppViewModel()
    MaterialTheme {
        WeatherPage(appViewModel)
    }
}

fun main() = application {
    val isOpen = rememberSaveable { mutableStateOf(true) }
    val showTray = rememberSaveable { mutableStateOf(true) }
    if (isOpen.value) {
        // 系统托盘及通知
        isOpen.value = BuildTray(isOpen, showTray)
        Window(
            onCloseRequest = {
                isOpen.value = false
            },
            title = "PlayWeather",
            state = rememberWindowState(width = 800.dp, height = 600.dp),
            icon = painterResource("image/launcher.png")
        ) {
            // Mac 中左上角菜单
            showTray.value = MenuBarWeather(isOpen, showTray)
            // content
            App()
        }
    }
}