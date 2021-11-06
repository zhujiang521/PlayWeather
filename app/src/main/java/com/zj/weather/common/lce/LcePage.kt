package com.zj.weather.common.lce

import androidx.compose.runtime.Composable
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess

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
    is PlaySuccess<T> -> {
        content(playState.data)
    }
}