package com.zj.weather.ui.view.list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.XLog
import com.zj.weather.utils.checkCoroutines
import com.zj.weather.utils.getDefaultLocale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherListViewModel @Inject constructor(
    application: Application,
    private val weatherListRepository: WeatherListRepository
) : AndroidViewModel(application) {

    private var language: Lang = getDefaultLocale(getApplication())
    private var nameToCityJob: Job? = null
    private var topCityJob: Job? = null
    private var insertCityJob: Job? = null

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
        nameToCityJob.checkCoroutines()
        nameToCityJob = viewModelScope.launch {
            val cityLookup = weatherListRepository.getGeoCityLookup(cityName)
            onLocationBeanListChanged(cityLookup)
        }
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        topCityJob.checkCoroutines()
        topCityJob = viewModelScope.launch {
            val cityLookup = weatherListRepository.getGeoTopCity(language)
            onLocationBeanListChanged(cityLookup)
        }
    }

    /**
     * 插入城市信息
     *
     * @param cityInfo 城市信息
     */
    fun insertCityInfo(cityInfo: CityInfo) {
        insertCityJob.checkCoroutines()
        insertCityJob = viewModelScope.launch(Dispatchers.IO) {
            weatherListRepository.insertCityInfo(cityInfo)
        }
    }

}