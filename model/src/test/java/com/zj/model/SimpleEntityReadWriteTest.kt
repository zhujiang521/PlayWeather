package com.zj.model

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.zj.model.room.PlayWeatherDatabase
import com.zj.model.room.dao.CityInfoDao
import com.zj.model.room.entity.CityInfo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class SimpleEntityReadWriteTest {
    private lateinit var cityInfoDao: CityInfoDao
    private lateinit var db: PlayWeatherDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, PlayWeatherDatabase::class.java
        ).build()
        cityInfoDao = db.cityInfoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val testDispatcher = TestCoroutineDispatcher()


    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
//        val user = CityInfo(name = "潞城")
//        cityInfoDao.insert(user)
//        val byName = cityInfoDao.getCityInfoForName("潞城")
//        assert(byName.name == user.name)
//        assertThat(user, equalTo(byName))
    }
}