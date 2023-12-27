package com.zj.weather.permission

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import android.os.Looper
import com.zj.utils.XLog
import com.zj.utils.isROrLater
import com.zj.utils.view.showLongToast
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import java.util.*


fun getLocation(
    context: Context,
    weatherViewModel: WeatherViewModel
) {
    if (!context.isPermissionGranted(Manifest.permission.READ_PHONE_STATE)) {
        XLog.d("READ_PHONE_STATE is no")
        return
    }
    if (!context.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
        XLog.d("ACCESS_FINE_LOCATION is no")
        return
    }
    if (!context.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)) {
        XLog.d("ACCESS_COARSE_LOCATION is no")
        return
    }
    //1.获取位置管理器
    val locationManager: LocationManager? =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?

    //2.获取位置提供器，GPS或是NetWork
    val providers: List<String> = locationManager?.getProviders(true) ?: arrayListOf()

    if (providers.isEmpty()) {
        XLog.i("getLocation: no location providers")
        showLongToast(context, "没有可用的位置提供器，请打开位置使用")
        return
    }

    XLog.i("getLocation: providers:$providers")
    val locationProvider: String?
    when {
        providers.contains(LocationManager.NETWORK_PROVIDER) -> {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER
            getCurrentLocation(locationManager, locationProvider, context, weatherViewModel)
            XLog.d("locationManager Network")
        }

        providers.contains(LocationManager.GPS_PROVIDER) -> {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER
            getCurrentLocation(locationManager, locationProvider, context, weatherViewModel)
            XLog.d("locationManager GPS")
        }

        else -> {
            XLog.i("getLocation: No location provider is available")
            showLongToast(context, "没有可用的位置提供器，请打开位置使用")
            return
        }
    }
}

@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    locationManager: LocationManager?,
    locationProvider: String,
    context: Context,
    weatherViewModel: WeatherViewModel
) {
    //3.获取当前位置，R以上的版本需要使用getCurrentLocation
    //  之前的版本可以可用requestSingleUpdate
    if (isROrLater) {
        locationManager?.getCurrentLocation(
            locationProvider,
            CancellationSignal(),
            context.mainExecutor
        ) { location ->
            getAddress(context, location, weatherViewModel)
        }
    } else {
        locationManager?.requestSingleUpdate(
            locationProvider,
            { location ->
                getAddress(context, location, weatherViewModel)
            }, Looper.getMainLooper()
        )
    }
}

//获取地址信息:城市、街道等信息
private fun getAddress(
    context: Context,
    location: Location?,
    weatherViewModel: WeatherViewModel,
): List<Address?>? {
    XLog.i(
        "Gets the current position：" + location?.longitude
            .toString() + "   " + location?.latitude
    )
    val result: List<Address?>?
    if (location == null) return null
    val gc = Geocoder(context, Locale.getDefault())
    result = gc.getFromLocation(
        location.latitude,
        location.longitude, 1
    )
    if (result != null) {
        weatherViewModel.updateCityInfo(location, result)
    }
    XLog.i("Obtaining Address Information：${result}")
    return result
}
