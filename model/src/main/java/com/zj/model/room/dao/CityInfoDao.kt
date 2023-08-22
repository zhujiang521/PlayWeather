package com.zj.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zj.model.room.entity.CityInfo


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/10/31
 * 描述：PlayAndroid
 *
 */
@Dao
interface CityInfoDao {

    @Query("SELECT * FROM city_info order by is_location desc,uid")
    fun getCityInfoList(): LiveData<List<CityInfo>>

    @Query("SELECT * FROM city_info where is_location = :isLocation")
    fun getIsLocationList(isLocation: Int = 1): List<CityInfo>

    @Query("SELECT COUNT(*) FROM city_info where name = :name")
    fun getHasLocation(name: String): Int

    @Query("SELECT * FROM city_info where is_index = 1")
    fun getIndexCity(): List<CityInfo>

    @Query("SELECT COUNT(*) FROM city_info")
    fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertList(cityInfoList: List<CityInfo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(cityInfo: CityInfo)

    @Update
    fun update(cityInfo: CityInfo): Int

    @Delete
    fun delete(cityInfo: CityInfo): Int

    @Delete
    fun deleteList(cityInfoList: List<CityInfo>): Int

    @Query("DELETE FROM city_info")
    fun deleteAll()
}
