package com.zj.weather.view.list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.model.city.GeoBean
import com.zj.model.city.GeoCacheBean
import com.zj.model.room.entity.CityInfo
import com.zj.utils.DEFAULT_CACHE_CITY_LIST
import com.zj.utils.XLog
import com.zj.utils.checkNetConnect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    application: Application,
    private val weatherListRepository: WeatherListRepository
) : AndroidViewModel(application) {

    private val _locationBeanList =
        MutableStateFlow<PlayState<List<GeoBean.LocationBean>>>(PlayLoading)
    val locationBeanList: Flow<PlayState<List<GeoBean.LocationBean>>> = _locationBeanList

    private fun onLocationBeanListChanged(hourlyBean: PlayState<List<GeoBean.LocationBean>>) {
        if (hourlyBean == _locationBeanList.value) {
            XLog.d("onLocationBeanListChanged no change")
            return
        }
        _locationBeanList.value = hourlyBean
    }


    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        if (!getApplication<Application>().checkNetConnect()) {
            onLocationBeanListChanged(PlayError(IllegalStateException("无网络链接")))
            return
        }
        viewModelScope.launch {
            val geoCityLookup = weatherListRepository.getGeoCityLookup(cityName)
            onLocationBeanListChanged(geoCityLookup)
        }
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        val cacheBean = Gson().fromJson(DEFAULT_CACHE_CITY_LIST, GeoCacheBean::class.java)
        onLocationBeanListChanged(PlaySuccess(cacheBean.list))
        XLog.i("Have a cache, return")
//        if (!getApplication<Application>().checkNetConnect()) {
//            onLocationBeanListChanged(PlayError(IllegalStateException("无网络链接")))
//            return
//        }
//        viewModelScope.launch {
//            // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
//            // 目前发现在S版本上有问题，R中没有发现
//            // 求人不如求自己，自己实现一套就行，没有那些问题
//            val geoCityLookup = weatherListRepository.getGeoTopCity()
//            onLocationBeanListChanged(geoCityLookup)
//        }
    }

    /**
     * 插入城市信息
     *
     * @param cityInfo 城市信息
     */
    fun insertCityInfo(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherListRepository.insertCityInfo(cityInfo)
        }
    }

}