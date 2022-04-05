package com.zj.weather.ui.view.weather.viewmodel

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.*
import com.qweather.sdk.bean.air.AirNowBean
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.weather.WeatherHourlyBean
import com.zj.weather.R
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
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

    companion object {
        private const val FIFTEEN_MINUTES = 60 * 1000 * 15
    }

    private var language: Lang = application.getDefaultLocale()
    private var weatherJob: Job? = null
    private var updateCityJob: Job? = null

    private val weatherMap = hashMapOf<String, Pair<Long, WeatherModel>>()


    val cityInfoList: LiveData<List<CityInfo>> = weatherRepository.refreshCityList()

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
        if (!getApplication<Application>().checkNetConnect()) {
            showToast(getApplication(), R.string.bad_network_view_tip)
            onWeatherModelChanged(PlayError(IllegalStateException("当前没有网络")))
            return
        }
        if (weatherMap.containsKey(location)) {
            val weather = weatherMap[location]
            if (weather != null && weather.first + FIFTEEN_MINUTES > System.currentTimeMillis()) {
                XLog.d("有东西了，直接返回")
                onWeatherModelChanged(PlaySuccess(weather.second))
                return
            }
        }
        weatherJob.checkCoroutines()
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
            val weatherNow = weatherRepository.getWeatherNow(location, language)
            // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
            // 目前发现在S版本上有问题，R中没有发现
            val weather24Hour = if (isSOrLater) {
                arrayListOf()
            } else {
                weatherRepository.getWeather24Hour(location, language)
            }
            val weather7Day = weatherRepository.getWeather7Day(location, language)
            val airNow = if (isSOrLater) {
                AirNowBean.NowBean().apply {
                    aqi = Random().nextInt(500).toString()
                }
            } else {
                weatherRepository.getAirNow(location, language)
            }
            val weatherModel = WeatherModel(
                nowBaseBean = weatherNow,
                hourlyBeanList = weather24Hour,
                dailyBean = weather7Day.first,
                dailyBeanList = weather7Day.second,
                airNowBean = airNow
            )
            weatherMap[location] = Pair(System.currentTimeMillis(), weatherModel)
            withContext(Dispatchers.Main) {
                onWeatherModelChanged(PlaySuccess(weatherModel))
            }
            XLog.e("获取天气:$location")
        }
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
            weatherRepository.updateCityInfo(location, result)
        }
    }

}