package com.zj.weather.room.dao

import androidx.room.*
import com.zj.weather.room.entity.CityInfo


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
    suspend fun getCityInfoList(): List<CityInfo>

    @Query("SELECT * FROM city_info where is_location = :isLocation")
    suspend fun getIsLocationList(isLocation: Int = 1): List<CityInfo>

    @Query("SELECT COUNT(*) FROM city_info where name = :name")
    suspend fun getHasLocation(name: String): Int

    @Query("SELECT COUNT(*) FROM city_info")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(cityInfoList: List<CityInfo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(CityInfo: CityInfo)

    @Update
    suspend fun update(CityInfo: CityInfo): Int

    @Delete
    suspend fun delete(CityInfo: CityInfo): Int

    @Delete
    suspend fun deleteList(cityInfoList: List<CityInfo>): Int

    @Query("DELETE FROM city_info")
    suspend fun deleteAll()
}
