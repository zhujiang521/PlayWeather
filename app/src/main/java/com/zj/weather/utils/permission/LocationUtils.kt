package com.zj.weather.utils.permission

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.zj.weather.ui.view.weather.viewmodel.WeatherViewModel
import com.zj.weather.utils.XLog
import java.util.*


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
    //3.获取上次的位置，一般第一次运行，此值为null
    val location: Location? = locationManager.getLastKnownLocation(locationProvider)
    XLog.v(
        "获取上次的位置-经纬度：" + location?.longitude
            .toString() + "   " + location?.latitude
    )
    getAddress(context, location,weatherViewModel)
}

//获取地址信息:城市、街道等信息
private fun getAddress(
    context: Context,
    location: Location?,
    weatherViewModel: WeatherViewModel,
): List<Address?>? {
    var result: List<Address?>? = null

    if (location == null) return result
    val gc = Geocoder(context, Locale.getDefault())
    result = gc.getFromLocation(
        location.latitude,
        location.longitude, 1
    )
    weatherViewModel.updateCityInfo(location, result)
    XLog.v("获取地址信息：${result[0]?.adminArea}")
    return result
}
