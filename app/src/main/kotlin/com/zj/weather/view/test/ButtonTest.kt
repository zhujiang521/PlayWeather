package com.zj.weather.view.test

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun ButtonTest() {
    val alertDialog = remember { mutableStateOf(true) }

    Column {
        Text(
            text = if (alertDialog.value) "zhujiang" else "liupeixing",
            modifier = Modifier.testTag("zhujiang")
        )
        Button(onClick = { alertDialog.value = !alertDialog.value }, modifier = Modifier.testTag("testClick")) {
            Text(text = "测试")
        }
    }
}