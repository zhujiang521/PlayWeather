package com.zj.weather

import android.app.Application
import android.location.Address
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.geo.GeoBean
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.NetCheckUtil
import com.zj.weather.utils.XLog
import com.zj.weather.utils.getDefaultLocale
import com.zj.weather.utils.showToast
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
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(application) {

    private var language: Lang = getDefaultLocale(getApplication())
    private val cityInfoDao = PlayWeatherDatabase.getDatabase(getApplication()).cityInfoDao()
    private var weatherJob: Job? = null
    private var nameToCityJob: Job? = null
    private var topCityJob: Job? = null
    private var insertCityJob: Job? = null
    private var updateCityJob: Job? = null
    private var deleteCityJob: Job? = null
    private var refreshCityJob: Job? = null

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

    fun resetLanguage() {
        language = getDefaultLocale(getApplication())
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

    fun getWeather(location: String = "CN101010100") {
        if (!NetCheckUtil.checkNet(getApplication())) {
            showToast(getApplication(), R.string.bad_network_view_tip)
            onWeatherModelChanged(PlayError(IllegalStateException("当前没有网络")))
            return
        }
        checkCoroutines(weatherJob)
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
            val weatherNow = mainRepository.getWeatherNow(location, language)
            val weather24Hour = mainRepository.getWeather24Hour(location, language)
            val weather7Day = mainRepository.getWeather7Day(location, language)
            val airNow = mainRepository.getAirNow(location, language)
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

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        checkCoroutines(nameToCityJob)
        nameToCityJob = viewModelScope.launch {
            val cityLookup = mainRepository.getGeoCityLookup(cityName)
            onLocationBeanListChanged(cityLookup)
        }
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        checkCoroutines(topCityJob)
        topCityJob = viewModelScope.launch {
            val cityLookup = mainRepository.getGeoTopCity(language)
            onLocationBeanListChanged(cityLookup)
        }
    }

    fun refreshCityList() {
        checkCoroutines(refreshCityJob)
        refreshCityJob = viewModelScope.launch(Dispatchers.IO) {
            var cityInfoList = cityInfoDao.getCityInfoList()
            cityInfoList = makeDefault(cityInfoList)
            withContext(Dispatchers.Main) {
                onCityInfoListChanged(cityInfoList)
            }
        }
    }

    fun makeDefault(cityInfoList: List<CityInfo>?): List<CityInfo> {
        return if (cityInfoList.isNullOrEmpty()) {
            val cityInfo = listOf(
                CityInfo(
                    location = "CN101010100",
                    name = getApplication<Application>().getString(R.string.default_location),
                )
            )
            cityInfo
        } else {
            XLog.e("cityInfoList:$cityInfoList")
            cityInfoList
        }
    }

    fun getSyncCityList(): List<CityInfo> {
        var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
        cityInfoList = makeDefault(cityInfoList)
        return cityInfoList
    }

    fun deleteCityInfo(cityInfo: CityInfo) {
        checkCoroutines(deleteCityJob)
        deleteCityJob = viewModelScope.launch(Dispatchers.IO) {
            cityInfoDao.delete(cityInfo)
            refreshCityList()
        }
    }

    fun hasLocation(): Boolean {
        val isLocation = runBlocking { cityInfoDao.getIsLocationList() }
        return isLocation.isNotEmpty()
    }

    fun getCount(): Int {
        return runBlocking { cityInfoDao.getCount() }
    }

    fun insertCityInfo(cityInfo: CityInfo, onSuccessListener: () -> Unit) {
        checkCoroutines(insertCityJob)
        insertCityJob = viewModelScope.launch(Dispatchers.IO) {
            val hasLocation = cityInfoDao.getHasLocation(cityInfo.name)
            if (hasLocation.isNullOrEmpty()) {
                cityInfoDao.insert(cityInfo)
                withContext(Dispatchers.Main) {
                    onSuccessListener()
                }
            } else {
                withContext(Dispatchers.Main) {
                    showToast(getApplication(), R.string.add_location_warn)
                }
            }
            refreshCityList()
        }
    }

    /**
     * 修改当前的位置信息
     *
     * @param location 位置
     * @param result Address
     */
    fun updateCityInfo(location: Location, result: MutableList<Address>) {
        checkCoroutines(updateCityJob)
        updateCityJob = viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateCityInfo(location, result)
            refreshCityList()
            withContext(Dispatchers.Main) {
                onSearchCityInfoChanged(0)
            }
        }
    }

    /**
     * 检查协程是否属于
     */
    private fun checkCoroutines(job: Job?) {
        if (job?.isActive != true) return
        job.cancel()
        XLog.d("已在查询，先取消之前的协程")
    }

}