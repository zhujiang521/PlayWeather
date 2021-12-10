package com.zj.weather.ui.view.weather.viewmodel

import android.app.Application
import android.content.Intent
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.zj.weather.R
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/11/02
 * 描述：PlayAndroid
 *
 */
@HiltViewModel
class WeatherViewModel @Inject constructor(
    application: Application,
    private val weatherRepository: WeatherRepository
) : AndroidViewModel(application) {

    private var language: Lang = getDefaultLocale(getApplication())
    private val cityInfoDao = PlayWeatherDatabase.getDatabase(getApplication()).cityInfoDao()
    private var weatherJob: Job? = null
    private var refreshCityJob: Job? = null
    private var updateCityJob: Job? = null

    private val _searchCityInfo = MutableLiveData(0)
    val searchCityInfo: LiveData<Int> = _searchCityInfo

    fun onSearchCityInfoChanged(page: Int) {
        if (page == _searchCityInfo.value) {
            XLog.d("onSearchCityInfoChanged no change")
            return
        }
        _searchCityInfo.postValue(page)
    }

    private val _cityInfoList = MutableLiveData(listOf<CityInfo>())
    val cityInfoList: LiveData<List<CityInfo>> = _cityInfoList

    private fun onCityInfoListChanged(list: List<CityInfo>) {
        if (list == _cityInfoList.value) {
            XLog.d("onCityInfoListChanged no change")
            return
        }
        _cityInfoList.postValue(list)
    }

    private val _weatherModel = MutableLiveData<PlayState<WeatherModel>>(PlayLoading)
    val weatherModel: LiveData<PlayState<WeatherModel>> = _weatherModel

    private fun onWeatherModelChanged(playState: PlayState<WeatherModel>) {
        if (playState == _weatherModel.value) {
            XLog.d("onWeatherModelChanged no change")
            return
        }
        _weatherModel.postValue(playState)
    }

    fun getWeather(location: String) {
        if (!NetCheckUtil.checkNet(getApplication())) {
            showToast(getApplication(), R.string.bad_network_view_tip)
            onWeatherModelChanged(PlayError(IllegalStateException("当前没有网络")))
            return
        }
        weatherJob.checkCoroutines()
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
            val weatherNow = weatherRepository.getWeatherNow(location, language)
            // val weather24Hour = weatherRepository.getWeather24Hour(location, language)
            val weather24Hour = arrayListOf<WeatherHourlyBean.HourlyBean>()
            val weather7Day = weatherRepository.getWeather7Day(location, language)
            // val airNow = weatherRepository.getAirNow(location, language)
            val airNow = AirNowBean.NowBean()
            val weatherModel = WeatherModel(
                nowBaseBean = weatherNow,
                hourlyBeanList = weather24Hour,
                dailyBean = weather7Day.first,
                dailyBeanList = weather7Day.second,
                airNowBean = airNow
            )
            withContext(Dispatchers.Main) {
                onWeatherModelChanged(PlaySuccess(weatherModel))
            }
            XLog.e("获取天气:$location")
        }
    }

    fun refreshCityList() {
        refreshCityJob.checkCoroutines()
        refreshCityJob = viewModelScope.launch(Dispatchers.IO) {
            val cityInfoList = weatherRepository.refreshCityList()
            withContext(Dispatchers.Main) {
                onCityInfoListChanged(cityInfoList)
                weatherRepository.updateCityIsIndex(cityInfoList) { index ->
                    onSearchCityInfoChanged(index)
                }
            }
        }
    }

    /**
     * 修改应该显示的城市
     */
    fun updateCityInfoIndex(cityInfo: CityInfo) {
        updateCityJob.checkCoroutines()
        updateCityJob = viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.updateCityIsIndex(cityInfo)
            refreshCityList()
        }
    }

    fun hasLocation(): Boolean {
        val isLocation = runBlocking { cityInfoDao.getIsLocationList() }
        return isLocation.isNotEmpty()
    }

    /**
     * 修改当前的位置信息
     *
     * @param location 位置
     * @param result Address
     */
    fun updateCityInfo(location: Location, result: MutableList<Address>) {
        updateCityJob.checkCoroutines()
        updateCityJob = viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.updateCityInfo(location, result) {
                refreshCityList()
                getApplication<Application>().sendBroadcast(Intent())
            }
        }
    }

}