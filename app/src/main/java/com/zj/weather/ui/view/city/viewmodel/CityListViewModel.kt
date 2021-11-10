package com.zj.weather.ui.view.city.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zj.weather.room.entity.CityInfo
import com.zj.weather.utils.XLog
import com.zj.weather.utils.checkCoroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CityListViewModel @Inject constructor(
    application: Application,
    private val cityListRepository: CityListRepository
) : AndroidViewModel(application) {

    private var refreshCityJob: Job? = null
    private var deleteCityJob: Job? = null
    private var updateCityJob: Job? = null

    private val _cityInfoList = MutableLiveData(listOf<CityInfo>())
    val cityInfoList: LiveData<List<CityInfo>> = _cityInfoList

    private fun onCityInfoListChanged(list: List<CityInfo>) {
        if (list == _cityInfoList.value) {
            XLog.d("onCityInfoListChanged no change")
            return
        }
        _cityInfoList.postValue(list)
    }

    /**
     * 刷新存储的城市列表
     */
    fun refreshCityList() {
        refreshCityJob.checkCoroutines()
        refreshCityJob = viewModelScope.launch(Dispatchers.IO) {
            val refreshCityList = cityListRepository.refreshCityList()
            withContext(Dispatchers.Main) {
                onCityInfoListChanged(refreshCityList)
            }
        }
        XLog.e("刷新了")
    }

    /**
     * 删除相应的城市信息
     */
    fun deleteCityInfo(cityInfo: CityInfo) {
        deleteCityJob.checkCoroutines()
        deleteCityJob = viewModelScope.launch(Dispatchers.IO) {
            cityListRepository.deleteCityInfo(cityInfo)
            refreshCityList()
        }
    }

    /**
     * 修改应该显示的城市
     */
    fun updateCityInfoIndex(cityInfo: CityInfo) {
        updateCityJob.checkCoroutines()
        updateCityJob = viewModelScope.launch(Dispatchers.IO) {
            cityListRepository.updateCityIsIndex(cityInfo)
            refreshCityList()
        }
    }

}