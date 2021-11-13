package com.zj.weather.utils.swipe

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset

/**
 * 左滑右滑对Item进行操作
 *
 * @param modifier 修饰符，不做描述
 * @param isShowChild 是否要显示操作子item
 * @param swipeStyle [SwipeStyle.EndToStart]:从结束往开始方向滑动 [SwipeStyle.StartToEnd]:从开始往结束方向滑动
 * @param childContent 子item
 * @param content item
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDeleteLayout(
    modifier: Modifier = Modifier,
    swipeState: SwipeableState<Int>,
    isShowChild: Boolean = true,
    swipeStyle: SwipeStyle = SwipeStyle.EndToStart,
    childContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var deleteWidth by remember {
        mutableStateOf(1)
    }

    var contentHeight by remember {
        mutableStateOf(1)
    }

    Box(
        modifier.swipeable(
            state = swipeState,
            anchors = mapOf(deleteWidth.toFloat() to 1, 0.7f to 0),
            thresholds = { _, _ ->
                FractionalThreshold(0.7f)
            },
            reverseDirection = swipeStyle == SwipeStyle.EndToStart,
            orientation = Orientation.Horizontal
        )
    ) {
        Box(modifier = Modifier
            .onGloballyPositioned {
                deleteWidth = it.size.width
            }
            .height(with(LocalDensity.current) { contentHeight.toDp() })
            .align(getDeleteAlign(swipeStyle))
        ) {
            childContent()
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                contentHeight = it.size.height
            }
            .offset {
                IntOffset(
                    if (isShowChild) {
                        if (swipeStyle == SwipeStyle.EndToStart) {
                            -swipeState.offset.value.toInt()
                        } else swipeState.offset.value.toInt()
                    } else {
                        0
                    }, 0
                )
            }
        ) {
            content()
        }
    }
}

/**
 * 获取删除的位置
 */
@Composable
private fun getDeleteAlign(swipeStyle: SwipeStyle) =
    if (swipeStyle == SwipeStyle.EndToStart) Alignment.CenterEnd else Alignment.CenterStart