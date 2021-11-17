package com.zj.weather.common.lce

import androidx.compose.runtime.Composable
import com.zj.weather.common.*

/**
 * 通过State进行控制的Loading、Content、Error页面
 *
 * @param playState 数据State
 * @param onErrorClick 错误时的点击事件
 * @param content 数据加载成功时应显示的可组合项
 */
@Composable
fun <T> LcePage(
    playState: PlayState<T>,
    onErrorClick: () -> Unit,
    content: @Composable (T) -> Unit
) = when (playState) {
    PlayLoading -> {
        LoadingContent()
    }
    is PlayError -> {
        ErrorContent(onErrorClick = onErrorClick)
    }
    is PlayNoContent -> {
        NoContent(tip = playState.reason)
    }
    is PlaySuccess<T> -> {
        content(playState.data)
    }
}