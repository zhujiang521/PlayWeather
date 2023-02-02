package com.zj.weather.view.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.LatLng
import com.zj.utils.XLog

@Composable
fun MapView() {
    val webView = rememberWebViewWithLifecycle()
    AndroidView(
        factory = {
            webView
        },
        modifier = Modifier.fillMaxSize(),
        update = {
            val map = it.map
            map.moveCamera(
                CameraUpdateFactory.newCameraPosition(
                    CameraPosition(
                        LatLng(40.7, 117.0),
                        15f,
                        0f,
                        0f,
                    )
                )
            )
        }
    )
}

@Composable
fun rememberWebViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val webView = remember {
        MapView(context)
    }
    val lifecycleObserver = rememberWebViewLifecycleObserver(webView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return webView
}

@Composable
private fun rememberWebViewLifecycleObserver(webView: MapView): LifecycleEventObserver =
    remember(webView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> webView.onCreate(null)
                Lifecycle.Event.ON_RESUME -> webView.onResume()
                Lifecycle.Event.ON_PAUSE -> webView.onPause()
                Lifecycle.Event.ON_DESTROY -> webView.onDestroy()
                else -> XLog.e("WebView", event.name)
            }
        }
    }