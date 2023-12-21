package com.zj.weather.view.weather.viewmodel

import android.app.Application
import android.content.Intent
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.zj.model.PlayError
import com.zj.model.PlayLoading
import com.zj.model.PlayState
import com.zj.model.PlaySuccess
import com.zj.model.WeatherModel
import com.zj.model.room.entity.CityInfo
import com.zj.model.weather.WeatherDailyBean
import com.zj.model.weather.WeatherNowBean
import com.zj.utils.XLog
import com.zj.utils.checkCoroutines
import com.zj.utils.checkNetConnect
import com.zj.utils.view.showToast
import com.zj.weather.R
import com.zj.weather.widget.today.LOCATION_REFRESH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.collections.set
import kotlin.system.measureTimeMillis

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

    private var weatherJob: Job? = null
    private var updateCityJob: Job? = null

    private val weatherMap = hashMapOf<String, Pair<Long, WeatherModel>>()

    val cityInfoList: Flow<List<CityInfo>> = weatherRepository.refreshCityList()

    private val _weatherModel = MutableStateFlow<PlayState<WeatherModel>>(PlayLoading)
    val weatherModel: Flow<PlayState<WeatherModel>> = _weatherModel.asStateFlow()

    private fun onWeatherModelChanged(playState: PlayState<WeatherModel>) {
        if (playState == _weatherModel.value) {
            XLog.d("onWeatherModelChanged no change")
            return
        }
        _weatherModel.value = playState
    }

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh(location: String) {
        // This doesn't handle multiple 'refreshing' tasks, don't use this
        viewModelScope.launch {
            // A fake 2 second 'refresh'
            _isRefreshing.emit(true)
            val time = measureTimeMillis {
                getWeather(location)
            }
            delay(if (time > 1000L) time else 1000L)
            _isRefreshing.emit(false)
        }
    }

    fun getWeather(location: String) {
        if (weatherMap.containsKey(location)) {
            val weather = weatherMap[location]
            if (weather != null && weather.first + FIFTEEN_MINUTES > System.currentTimeMillis()) {
                XLog.d("Direct return")
                onWeatherModelChanged(PlaySuccess(weather.second))
                return
            }
        }
        if (!getApplication<Application>().checkNetConnect()) {
            showToast(getApplication(), R.string.bad_network_view_tip)
            onWeatherModelChanged(PlayError(IllegalStateException("当前没有网络")))
            return
        }
        weatherJob.checkCoroutines()
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
            val weatherNow = weatherRepository.getWeatherNow(location)
            // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
            // 目前发现在S版本上有问题，R中没有发现
            val weather24Hour = weatherRepository.getWeather24Hour(location)
            val weather7Day = weatherRepository.getWeather7Day(location)
            val airNow = weatherRepository.getAirNow(location)
            val weatherLifeIndicesList = weatherRepository.getWeatherLifeIndicesList(location)
            buildWeekWeather(weather7Day?.second, weatherNow)
            val weatherModel = WeatherModel(
                nowBaseBean = weatherNow,
                hourlyBeanList = weather24Hour,
                dailyBean = weather7Day?.first,
                dailyBeanList = weather7Day?.second ?: arrayListOf(),
                airNowBean = airNow,
                weatherLifeList = weatherLifeIndicesList
            )
            weatherMap[location] = Pair(System.currentTimeMillis(), weatherModel)
            withContext(Dispatchers.Main) {
                onWeatherModelChanged(PlaySuccess(weatherModel))
            }
            XLog.w("For the weather:$location")
        }
    }

    /**
     * 为了构建7天天气的柱状图
     */
    private fun buildWeekWeather(
        second: List<WeatherDailyBean.DailyBean>?,
        weatherNow: WeatherNowBean.NowBaseBean?
    ) {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        second?.forEach {
            val currentMin = it.tempMin?.toInt() ?: 0
            if (min > currentMin) {
                min = currentMin
            }

            val currentMax = it.tempMax?.toInt() ?: 0
            if (max < currentMax) {
                max = currentMax
            }
        }

        second?.forEachIndexed { index, dailyBean ->
            dailyBean.weekMin = min
            dailyBean.weekMax = max

            if (index == 0) {
                dailyBean.fxDate = "今天"
            } else {
                dailyBean.fxDate = dailyBean.fxDate ?: "今天"
            }

            if (dailyBean.fxDate == "今天") {
                dailyBean.temp = weatherNow?.temp?.toInt() ?: -100
            }
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
            getApplication<Application>().sendBroadcast(Intent(LOCATION_REFRESH))
        }
    }

}