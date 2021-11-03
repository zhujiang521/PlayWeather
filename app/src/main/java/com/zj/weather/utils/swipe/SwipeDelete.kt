package com.zj.weather.utils.swipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 滑动删除
 *
 * @param onDelete 删除事件
 * @param dismissContent 关闭的组件
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDelete(onDelete: () -> Unit, dismissContent: @Composable RowScope.() -> Unit) {
    val rememberDismissState = rememberDismissState()
    if (rememberDismissState.isDismissed(DismissDirection.EndToStart)) {
        onDelete()
    }
    SwipeToDismiss(
        modifier = Modifier
            .fillMaxWidth(),
        state = rememberDismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Red),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Delete", modifier = Modifier.padding(end = 20.dp))
            }
        }) {
        dismissContent()
    }

}