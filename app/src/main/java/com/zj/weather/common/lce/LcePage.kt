package com.zj.weather.common.lce

import androidx.compose.runtime.Composable
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess

@Composable
fun <T> LcePage(
    playState: PlayState<T>,
    onErrorClick: () -> Unit,
    content: @Composable (T) -> Unit
) {

    when (playState) {
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

}