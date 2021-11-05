package com.zj.weather.common.lce

import androidx.compose.runtime.Composable
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess

@Composable
fun LcePage(playState: PlayState, onErrorClick: () -> Unit, content: @Composable () -> Unit) {

    when (playState) {
        PlayLoading -> {
            LoadingContent()
        }
        is PlayError -> {
            ErrorContent(onErrorClick = onErrorClick)
        }
        is PlaySuccess -> {
            content()
        }
    }

}