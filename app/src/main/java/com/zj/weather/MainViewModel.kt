package com.zj.weather

import android.app.Application
import android.location.Address
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.qweather.sdk.bean.base.Code
import com.qweather.sdk.bean.base.Lang
import com.qweather.sdk.bean.base.Range
import com.qweather.sdk.bean.geo.GeoBean
import com.qweather.sdk.view.QWeather.*
import com.zj.weather.common.PlayError
import com.zj.weather.common.PlayLoading
import com.zj.weather.common.PlayState
import com.zj.weather.common.PlaySuccess
import com.zj.weather.model.WeatherModel
import com.zj.weather.room.PlayWeatherDatabase
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/11/02
 * 描述：PlayAndroid
 *
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private var language: Lang = getDefaultLocale(getApplication())
    private val cityInfoDao = PlayWeatherDatabase.getDatabase(getApplication()).cityInfoDao()

    private val _locationBeanList =
        MutableLiveData<PlayState<List<GeoBean.LocationBean>>>(PlayLoading)
    val locationBeanList: LiveData<PlayState<List<GeoBean.LocationBean>>> = _locationBeanList

    private fun onLocationBeanListChanged(hourlyBean: PlayState<List<GeoBean.LocationBean>>) {
        if (_locationBeanList.value == hourlyBean) {
            return
        }
        _locationBeanList.value = hourlyBean
    }

    private val _searchCityInfo = MutableLiveData(0)
    val searchCityInfo: LiveData<Int> = _searchCityInfo

    fun onSearchCityInfoChanged(page: Int) {
        if (_searchCityInfo.value == page) {
            return
        }
        _searchCityInfo.value = page
    }

    private val _cityInfoList = MutableLiveData(listOf<CityInfo>())
    val cityInfoList: LiveData<List<CityInfo>> = _cityInfoList

    private fun onCityInfoListChanged(list: List<CityInfo>) {
        if (_cityInfoList.value == list) {
            return
        }
        _cityInfoList.value = list
    }

    fun resetLanguage() {
        language = getDefaultLocale(getApplication())
    }

    private val _weatherModel = MutableLiveData<PlayState<WeatherModel>>(PlayLoading)
    val weatherModel: LiveData<PlayState<WeatherModel>> = _weatherModel

    private fun onWeatherModelChanged(playState: PlayState<WeatherModel>) {
        if (_weatherModel.value == playState) {
            return
        }
        _weatherModel.value = playState
    }

    fun getWeather(location: String = "CN101010100") {
        Log.e(TAG, "getWeather: location:$location")
        if (!NetCheckUtil.checkNet(getApplication())) {
            showToast(getApplication(), R.string.bad_network_view_tip)
            onWeatherModelChanged(PlayError(IllegalStateException("当前没有网络")))
            return
        }
        onWeatherModelChanged(PlayLoading)
        viewModelScope.launch(Dispatchers.IO) {
            val mainRepository = MainRepository(getApplication())
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
        }
    }

    /**
     * 根据城市名称进行模糊查询
     *
     * @param cityName 城市名称
     */
    fun getGeoCityLookup(cityName: String = "北京") {
        val mainRepository = MainRepository(getApplication())
        viewModelScope.launch {
            val cityLookup = mainRepository.getGeoCityLookup(cityName)
            onLocationBeanListChanged(cityLookup)
        }
    }

    /**
     * 热门城市信息查询
     */
    fun getGeoTopCity() {
        val mainRepository = MainRepository(getApplication())
        viewModelScope.launch {
            val cityLookup = mainRepository.getGeoTopCity(language)
            onLocationBeanListChanged(cityLookup)
        }
    }

    fun refreshCityList() {
        viewModelScope.launch(Dispatchers.IO) {
            var cityInfoList = cityInfoDao.getCityInfoList()
            if (cityInfoList.isNullOrEmpty()) {
                cityInfoList = listOf(
                    CityInfo(
                        location = "CN101010100",
                        name = getApplication<Application>().getString(R.string.default_location),
                    )
                )
            } else {
                Log.e(TAG, "NavGraph: cityInfoList:$cityInfoList")
            }
            withContext(Dispatchers.Main) {
                onCityInfoListChanged(cityInfoList)
            }
        }
    }

    fun getSyncCityList(): List<CityInfo> {
        var cityInfoList = runBlocking { cityInfoDao.getCityInfoList() }
        if (cityInfoList.isNullOrEmpty()) {
            cityInfoList = listOf(
                CityInfo(
                    location = "CN101010100",
                    name = getApplication<Application>().getString(R.string.default_location)
                )
            )
        } else {
            Log.e(TAG, "NavGraph: cityInfoList:$cityInfoList")
        }
        return cityInfoList
    }

    fun deleteCityInfo(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun insertCityInfo(cityInfo: CityInfo, onSuccessListener: () -> kotlin.Unit) {
        viewModelScope.launch(Dispatchers.IO) {
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
        val mainRepository = MainRepository(getApplication())
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateCityInfo(location, result)
            refreshCityList()
            withContext(Dispatchers.Main) {
                onSearchCityInfoChanged(0)
            }
        }
    }

}