package com.zj.weather.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2021/10/31
 * 描述：PlayAndroid
 *
 */
@Entity(tableName = "city_info")
data class CityInfo(
    @PrimaryKey(autoGenerate = true) var uid: Int = 0,
    @ColumnInfo(name = "location") val location: String = "",
    @ColumnInfo(name = "location_id") val locationId: String = "",
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "province") val province: String = "",
    @ColumnInfo(name = "city") val city: String = "",
    @ColumnInfo(name = "is_location") val isLocation: Int = 0,
    @ColumnInfo(name = "is_index") var isIndex: Int = 0,
)