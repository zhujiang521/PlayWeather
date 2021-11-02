package com.zj.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.dao.CityInfoDao
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.ui.permission.isPermissionsGranted
import com.zj.weather.ui.permission.onAlertDialog
import com.zj.weather.ui.theme.PlayWeatherTheme
import com.zj.weather.utils.setAndroidNativeLightStatusBar
import com.zj.weather.utils.transparentStatusBar
import kotlinx.coroutines.*
import java.util.*

class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {

    companion object {
        private const val TAG = "MainActivity"
        private const val LOCATION_CODE = 301
    }

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var cityInfoDao: CityInfoDao
    private var locationManager: LocationManager? = null
    private var locationProvider: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transparentStatusBar()
        setAndroidNativeLightStatusBar()
        cityInfoDao = PlayWeatherDatabase.getDatabase(this).cityInfoDao()
        getLocation()
        checkLocationPermission()
        var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
        if (cityInfoList.isNullOrEmpty()) {
            cityInfoList = listOf(
                CityInfo(
                    location = "CN101010100",
                    name = getString(R.string.default_location)
                )
            )
        }
        mainViewModel.getWeather(cityInfoList[0].location)
        setContent {
            PlayWeatherTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavGraph(mainViewModel = mainViewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.resetLanguage()
    }

    private fun checkLocationPermission() {
        //获取权限（如果没有开启权限，会弹出对话框，询问是否开启权限）
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            //请求权限
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), LOCATION_CODE
            )
        } else {
            getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //1.获取位置管理器
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //2.获取位置提供器，GPS或是NetWork
        val providers: List<String> = locationManager?.getProviders(true) ?: arrayListOf()
        Log.e(TAG, "getLocation: providers:$providers")
        when {
            providers.contains(LocationManager.GPS_PROVIDER) -> {
                //如果是GPS
                locationProvider = LocationManager.GPS_PROVIDER
                Log.v(TAG, "定位方式GPS")
            }
            providers.contains(LocationManager.NETWORK_PROVIDER) -> {
                //如果是Network
                locationProvider = LocationManager.NETWORK_PROVIDER
                Log.v(TAG, "定位方式Network")
            }
            else -> {
                Log.e(TAG, "getLocation: 没有可用的位置提供器")
                return
            }
        }
        //3.获取上次的位置，一般第一次运行，此值为null
        val location: Location? = locationManager?.getLastKnownLocation(locationProvider!!)
        Log.v(
            TAG,
            "获取上次的位置-经纬度：" + location?.longitude
                .toString() + "   " + location?.latitude
        )
        getAddress(location)
    }

    private var locationListener: LocationListener = LocationListener { location ->
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        Log.v(
            TAG,
            "监视地理位置变化-经纬度：" + location.longitude
                .toString() + "   " + location.latitude
        )
    }


    //获取地址信息:城市、街道等信息
    private fun getAddress(location: Location?): List<Address?>? {
        var result: List<Address?>? = null

        if (location == null) return result
        val gc = Geocoder(this, Locale.getDefault())
        result = gc.getFromLocation(
            location.latitude,
            location.longitude, 1
        )
        launch(Dispatchers.IO) {
            updateCityInfo(location, result)
        }
        Log.v(TAG, "获取地址信息：${result[0]?.adminArea}")

        return result
    }

    private suspend fun updateCityInfo(
        location: Location,
        result: MutableList<Address>
    ) {
        val isLocationList = cityInfoDao.getIsLocationList()
        val cityInfo = CityInfo(
            location = "${location.longitude},${
                location.latitude
            }", name = result[0].adminArea ?: "",
            isLocation = 1
        )
        if (isLocationList.isNotEmpty()) {
            Log.d(TAG, "updateCityInfo: 数据库中没有当前的数据，需要新增")
            cityInfoDao.update(cityInfo)
        } else {
            cityInfoDao.insert(cityInfo)
            Log.d(TAG, "updateCityInfo: 数据库中已经存在当前的数据，需要修改")
        }
        withContext(Dispatchers.Main) {
            mainViewModel.onSearchCityInfoChanged(1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_CODE || grantResults.isEmpty()
            || !isPermissionsGranted(this, permissions)
        ) {
            onAlertDialog(this)
            return
        }
        getLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(locationListener)
    }

}