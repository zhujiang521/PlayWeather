package com.zj.weather.common.permission

import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import android.os.Build
import android.os.CancellationSignal
import android.os.Looper
import com.zj.weather.view.weather.viewmodel.WeatherViewModel
import com.zj.utils.XLog
import java.util.*
import java.util.function.Consumer


@SuppressLint("MissingPermission")
fun getLocation(context: Context, weatherViewModel: WeatherViewModel) {
    val locationManager: LocationManager?
    val locationProvider: String?

    //1.获取位置管理器
    locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    //2.获取位置提供器，GPS或是NetWork
    val providers: List<String> = locationManager.getProviders(true)
    XLog.e("getLocation: providers:$providers")
    when {
        providers.contains(LocationManager.NETWORK_PROVIDER) -> {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER
            XLog.d("定位方式Network")
        }
        providers.contains(LocationManager.GPS_PROVIDER) -> {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER
            XLog.d("定位方式GPS")
        }
        else -> {
            XLog.e("getLocation: 没有可用的位置提供器")
            return
        }
    }

    //3.获取当前位置，R以上的版本需要使用getCurrentLocation
    //  之前的版本可以可用requestSingleUpdate
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        locationManager.getCurrentLocation(
            locationProvider,
            CancellationSignal(),
            context.mainExecutor,
            Consumer { location ->
                getAddress(context, location, weatherViewModel)
            })
    } else {
        locationManager.requestSingleUpdate(
            locationProvider,
            LocationListener { location ->
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
    XLog.e(
        "获取当前位置-经纬度：" + location?.longitude
            .toString() + "   " + location?.latitude
    )
    var result: List<Address?>? = null
    if (location == null) return result
    val gc = Geocoder(context, Locale.getDefault())
    result = gc.getFromLocation(
        location.latitude,
        location.longitude, 1
    )
    weatherViewModel.updateCityInfo(location, result)
    XLog.e("获取地址信息：${result}")
    return result
}
