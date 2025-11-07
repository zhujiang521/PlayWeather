package com.zj.weather.view.weather.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.zj.weather.R

@Composable
fun HeaderAction(modifier: Modifier = Modifier, cityListClick: () -> Unit, cityList: () -> Unit) {
    Row(modifier = modifier.wrapContentWidth(Alignment.End)) {
        IconButton(
            modifier = Modifier.wrapContentWidth(Alignment.End),
            onClick = cityListClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_add),
                contentDescription = "add"
            )
        }
        IconButton(
            modifier = Modifier
                .wrapContentWidth(Alignment.Start), onClick = cityList
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_format_list_bulleted),
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