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
import com.zj.utils.checkNetConnect
import com.zj.utils.view.showToast
import com.zj.utils.weather.getLocationForCityInfo
import com.zj.weather.R
import com.zj.weather.widget.today.LOCATION_REFRESH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
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
        // 缓存过期时间，暂定为15分钟
        private const val FIFTEEN_MINUTES = 60 * 1000 * 15
    }

    private val weatherMap = hashMapOf<String, Pair<Long, WeatherModel>>()

    val cityInfoList: Flow<List<CityInfo>> = weatherRepository.refreshCityList()

    // 刷新的flow，用于记录下拉刷新状态
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    fun refresh(cityInfo: CityInfo) {
        // This doesn't handle multiple 'refreshing' tasks, don't use this
        viewModelScope.launch {
            // A fake 2 second 'refresh'
            _isRefreshing.emit(true)
            val time = measureTimeMillis {
                weatherModel(cityInfo, true)
            }
            delay(if (time > 1000L) time else 1000L)
            _isRefreshing.emit(false)
        }
    }


    /**
     * 综合起来直接调用，即数据观察、数据请求、重试等都使用这个函数来进行
     *
     * @param cityInfo 城市信息
     * @param refresh 是否要刷新，默认为否，否的话就是使用缓存数据
     *
     * @return flow，可观测数据流
     */
    fun weatherModel(cityInfo: CityInfo, refresh: Boolean = false): Flow<PlayState<WeatherModel>> =
        channelFlow {
            val location = getLocationForCityInfo(cityInfo)
            if (weatherMap.containsKey(location) && !refresh) {
                val weather = weatherMap[location]
                if (weather != null && weather.first + FIFTEEN_MINUTES > System.currentTimeMillis()) {
                    XLog.d("Direct return")
                    send(PlaySuccess(weather.second))
                }
            }
            if (!getApplication<Application>().checkNetConnect()) {
                showToast(getApplication(), R.string.bad_network_view_tip)
                send(PlayError(IllegalStateException("当前没有网络")))
            }
            val weatherNow =
                async(Dispatchers.IO) { weatherRepository.getWeatherNow(location) }.await()
            // 这块由于这两个接口有问题，和风天气的jar包问题，提交反馈人家说没问题。。qtmd。
            // 目前发现在S版本上有问题，R中没有发现
            val weather24Hour =
                async(Dispatchers.IO) { weatherRepository.getWeather24Hour(location) }.await()
            val weather7Day =
                async(Dispatchers.IO) { weatherRepository.getWeather7Day(location) }.await()
            val airNow = async(Dispatchers.IO) { weatherRepository.getAirNow(location) }.await()
            val weatherLifeIndicesList =
                async { weatherRepository.getWeatherLifeIndicesList(location) }.await()
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
            send(PlaySuccess(weatherModel))
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
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.updateCityInfo(location, result)
            getApplication<Application>().sendBroadcast(Intent(LOCATION_REFRESH))
        }
    }

}