package com.zj.weather

import com.zj.utils.Lunar
import org.junit.Test

import org.junit.Assert.*
import java.util.Calendar

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
        val calendar = Calendar.getInstance()
        val lunar = Lunar(calendar)
        assertEquals(false, lunar.isGhostFestival())

        calendar[Calendar.MONTH] = 7
        calendar[Calendar.DAY_OF_MONTH] = 18
        val newLunar = Lunar(calendar)
        assertEquals(true, newLunar.isGhostFestival())
    }
}