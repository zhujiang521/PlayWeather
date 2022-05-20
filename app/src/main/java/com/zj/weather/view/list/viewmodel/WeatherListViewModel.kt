package com.zj.weather.view.list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.model.city.GeoBean
import com.zj.utils.XLog
import com.zj.utils.checkNetConnect
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.model.room.entity.CityInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    application: Application,
    private val weatherListRepository: WeatherListRepository
) : AndroidViewModel(application) {

    private val _locationBeanList =
        MutableLiveData<PlayState<List<GeoBean.LocationBean>>>(PlayLoading)
    val locationBeanList: LiveData<PlayState<List<GeoBean.LocationBean>>> = _locationBeanList

    private fun onLocationBeanListChanged(hourlyBean: PlayState<List<GeoBean.LocationBean>>) {
        if (hourlyBean == _locationBeanList.value) {
            XLog.d("onLocationBeanListChanged no change")
            return
        }
        _locationBeanList.postValue(hourlyBean)
    }


    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        if (!getApplication<Application>().checkNetConnect()) {
            onLocationBeanListChanged(PlayError(IllegalStateException("无网络链接")))
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
        viewModelScope.launch {
            // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
            // 目前发现在S版本上有问题，R中没有发现
            val geoCityLookup = weatherListRepository.getGeoTopCity()
            onLocationBeanListChanged(geoCityLookup)
        }
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