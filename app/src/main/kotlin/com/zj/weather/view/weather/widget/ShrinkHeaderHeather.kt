package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun HeaderAction(modifier: Modifier = Modifier, cityListClick: () -> Unit, cityList: () -> Unit) {
    Row(modifier = modifier.wrapContentWidth(Alignment.End)) {
        IconButton(
            modifier = Modifier.wrapContentWidth(Alignment.End),
            onClick = cityListClick
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "add"
            )
        }
        IconButton(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start), onClick = cityList
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.List,
                contentDescription = "list"
            )
        }
    }
}

@Preview(showBackground = false, name = "头部事件")
@Composable
fun HeaderActionPreview() {
    HeaderAction(Modifier, {}, {})
}