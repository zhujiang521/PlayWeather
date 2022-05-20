package com.zj.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zj.model.room.dao.CityInfoDao
import com.zj.model.room.entity.CityInfo


/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：2020/9/10
 * 描述：PlayAndroid
 *
 */
@Database(
    entities = [CityInfo::class],
    version = 1
)
abstract class PlayWeatherDatabase : RoomDatabase() {

    abstract fun cityInfoDao(): CityInfoDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PlayWeatherDatabase? = null

        fun getDatabase(context: Context): PlayWeatherDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlayWeatherDatabase::class.java,
                    "play_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}