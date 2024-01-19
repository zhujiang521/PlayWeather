package com.zj.weather.view.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.CameraPosition
import com.amap.api.maps2d.model.LatLng
import com.amap.api.maps2d.model.Marker
import com.amap.api.maps2d.model.MarkerOptions
import com.zj.utils.XLog
import com.zj.utils.checkNetConnect
import com.zj.utils.lce.ErrorContent
import com.zj.utils.view.showToast
import com.zj.weather.R
import com.zj.weather.view.city.widget.TitleBar


@Composable
fun MapView(latitude: Double = 40.0, longitude: Double = 117.0, onBack: () -> Unit) {
    val mapView = rememberMapViewWithLifecycle()
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // 标题栏
        TitleBar(R.string.map_title, onBack)
        if (!context.checkNetConnect()) {
            showToast(context, R.string.bad_network_view_tip)
            ErrorContent(Modifier.fillMaxSize()) {}
            return
        }
        AndroidView(
            factory = {
                mapView
            },
            modifier = Modifier.fillMaxSize(),
            update = {
                val map = it.map
                map?.moveCamera(
                    CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(latitude, longitude),
                            15f,
                            0f,
                            0f,
                        )
                    )
                )
                map?.uiSettings?.apply {
                    // 设置旋转手势是否可用。
//                    isRotateGesturesEnabled = false
                    // 设置所有手势不可用
                    setAllGesturesEnabled(false)
                    // 设置缩放按钮是否可见。
                    isZoomControlsEnabled = false
                    // 设置双指缩放手势是否可用。
                    isZoomGesturesEnabled = true
                    // 设置是否以地图中心点缩放
//                    isGestureScaleByMapCenter = true
                }
                val markerOptions = MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    ?.draggable(true)
                val marker: Marker = map.addMarker(markerOptions)
                marker.showInfoWindow()
            }
        )
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }
    val lifecycleObserver = rememberMapViewLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
    return mapView
}

@Composable
private fun rememberMapViewLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(null)
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> XLog.e("WebView", event.name)
            }
        }
    }