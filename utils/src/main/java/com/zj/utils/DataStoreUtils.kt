package com.zj.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.io.IOException

/**
 * 版权：Zhujiang 个人版权
 *
 * @author zhujiang
 * 创建日期：12/3/20
 *
 * 异步获取数据
 * [getData] [readBooleanFlow] [readFloatFlow] [readIntFlow] [readLongFlow] [readStringFlow]
 * 同步获取数据
 * [getSyncData] [readBooleanData] [readFloatData] [readIntData] [readLongData] [readStringData]
 *
 * 异步写入数据
 * [putData] [saveBooleanData] [saveFloatData] [saveIntData] [saveLongData] [saveStringData]
 * 同步写入数据
 * [putSyncData] [saveSyncBooleanData] [saveSyncFloatData] [saveSyncIntData] [saveSyncLongData] [saveSyncStringData]
 *
 * 异步清除数据
 * [clear]
 * 同步清除数据
 * [clearSync]
 *
 * 描述：DataStore 工具类
 *
 */

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "PlayAndroidDataStore")

const val DEFAULT_CACHE_CITY_LIST = "{\"list\":[{\"adm1\":\"浙江省\",\"adm2\":\"杭州\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/32t1\",\"hasLocation\":false,\"id\":\"101210106\",\"isDst\":\"0\",\"lat\":\"30.27371\",\"lon\":\"119.97874\",\"name\":\"余杭\",\"rank\":\"25\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"北京市\",\"adm2\":\"北京\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2az1\",\"hasLocation\":false,\"id\":\"101010300\",\"isDst\":\"0\",\"lat\":\"39.92149\",\"lon\":\"116.48641\",\"name\":\"朝阳\",\"rank\":\"15\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"北京市\",\"adm2\":\"北京\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2ax1\",\"hasLocation\":false,\"id\":\"101010100\",\"isDst\":\"0\",\"lat\":\"39.90499\",\"lon\":\"116.40529\",\"name\":\"北京\",\"rank\":\"10\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"上海市\",\"adm2\":\"上海\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2bf1\",\"hasLocation\":true,\"id\":\"101020500\",\"isDst\":\"0\",\"lat\":\"31.38352\",\"lon\":\"121.25034\",\"name\":\"嘉定\",\"rank\":\"23\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"北京市\",\"adm2\":\"北京\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2ay1\",\"hasLocation\":false,\"id\":\"101010200\",\"isDst\":\"0\",\"lat\":\"39.95607\",\"lon\":\"116.31032\",\"name\":\"海淀\",\"rank\":\"15\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"河南省\",\"adm2\":\"郑州\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/1tst1\",\"hasLocation\":false,\"id\":\"101180112\",\"isDst\":\"0\",\"lat\":\"34.77584\",\"lon\":\"113.68604\",\"name\":\"金水\",\"rank\":\"25\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"浙江省\",\"adm2\":\"杭州\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/32p1\",\"hasLocation\":false,\"id\":\"101210102\",\"isDst\":\"0\",\"lat\":\"30.16293\",\"lon\":\"120.27069\",\"name\":\"萧山\",\"rank\":\"23\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"广东省\",\"adm2\":\"深圳\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/3i71\",\"hasLocation\":false,\"id\":\"101280601\",\"isDst\":\"0\",\"lat\":\"22.54700\",\"lon\":\"114.08595\",\"name\":\"深圳\",\"rank\":\"13\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"上海市\",\"adm2\":\"上海\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2bc1\",\"hasLocation\":false,\"id\":\"101020100\",\"isDst\":\"0\",\"lat\":\"31.23171\",\"lon\":\"121.47264\",\"name\":\"上海\",\"rank\":\"11\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"},{\"adm1\":\"上海市\",\"adm2\":\"上海\",\"country\":\"中国\",\"fxLink\":\"http://hfx.link/2bg1\",\"hasLocation\":false,\"id\":\"101020600\",\"isDst\":\"0\",\"lat\":\"31.24594\",\"lon\":\"121.56770\",\"name\":\"浦东新区\",\"rank\":\"15\",\"type\":\"city\",\"tz\":\"Asia/Shanghai\",\"utcOffset\":\"+08:00\"}]}"

object DataStoreUtils {

    private lateinit var dataStore: DataStore<Preferences>

    /**
     * init Context
     * @param context Context
     */
    fun init(context: Context) {
        dataStore = context.dataStore
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getSyncData(key: String, default: U): U {
        val res = when (default) {
            is Long -> readLongData(key, default)
            is String -> readStringData(key, default)
            is Int -> readIntData(key, default)
            is Boolean -> readBooleanData(key, default)
            is Float -> readFloatData(key, default)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return res as U
    }

    @Suppress("UNCHECKED_CAST")
    fun <U> getData(key: String, default: U): Flow<U> {
        val data = when (default) {
            is Long -> readLongFlow(key, default)
            is String -> readStringFlow(key, default)
            is Int -> readIntFlow(key, default)
            is Boolean -> readBooleanFlow(key, default)
            is Float -> readFloatFlow(key, default)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
        return data as Flow<U>
    }

    suspend fun <U> putData(key: String, value: U) {
        when (value) {
            is Long -> saveLongData(key, value)
            is String -> saveStringData(key, value)
            is Int -> saveIntData(key, value)
            is Boolean -> saveBooleanData(key, value)
            is Float -> saveFloatData(key, value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    fun <U> putSyncData(key: String, value: U) {
        when (value) {
            is Long -> saveSyncLongData(key, value)
            is String -> saveSyncStringData(key, value)
            is Int -> saveSyncIntData(key, value)
            is Boolean -> saveSyncBooleanData(key, value)
            is Float -> saveSyncFloatData(key, value)
            else -> throw IllegalArgumentException("This type can be saved into DataStore")
        }
    }

    fun readBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> =
        dataStore.data
            .catch {
                //当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
                //但是如果是其他的异常，最好将它抛出去，不要隐藏问题
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[booleanPreferencesKey(key)] ?: default
            }

    fun readBooleanData(key: String, default: Boolean = false): Boolean {
        var value = false
        runBlocking {
            dataStore.data.first {
                value = it[booleanPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readIntFlow(key: String, default: Int = 0): Flow<Int> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[intPreferencesKey(key)] ?: default
            }

    fun readIntData(key: String, default: Int = 0): Int {
        var value = 0
        runBlocking {
            dataStore.data.first {
                value = it[intPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readStringFlow(key: String, default: String = ""): Flow<String> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[stringPreferencesKey(key)] ?: default
            }

    fun readStringData(key: String, default: String = ""): String {
        var value = ""
        runBlocking {
            dataStore.data.first {
                value = it[stringPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readFloatFlow(key: String, default: Float = 0f): Flow<Float> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[floatPreferencesKey(key)] ?: default
            }

    fun readFloatData(key: String, default: Float = 0f): Float {
        var value = 0f
        runBlocking {
            dataStore.data.first {
                value = it[floatPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    fun readLongFlow(key: String, default: Long = 0L): Flow<Long> =
        dataStore.data
            .catch {
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[longPreferencesKey(key)] ?: default
            }

    fun readLongData(key: String, default: Long = 0L): Long {
        var value = 0L
        runBlocking {
            dataStore.data.first {
                value = it[longPreferencesKey(key)] ?: default
                true
            }
        }
        return value
    }

    suspend fun saveBooleanData(key: String, value: Boolean) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    fun saveSyncBooleanData(key: String, value: Boolean) =
        runBlocking { saveBooleanData(key, value) }

    suspend fun saveIntData(key: String, value: Int) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    fun saveSyncIntData(key: String, value: Int) = runBlocking { saveIntData(key, value) }

    suspend fun saveStringData(key: String, value: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    fun saveSyncStringData(key: String, value: String) = runBlocking { saveStringData(key, value) }

    suspend fun saveFloatData(key: String, value: Float) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
    }

    fun saveSyncFloatData(key: String, value: Float) = runBlocking { saveFloatData(key, value) }

    suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    fun saveSyncLongData(key: String, value: Long) = runBlocking { saveLongData(key, value) }

    suspend fun clear() {
        dataStore.edit {
            it.clear()
        }
    }

    fun clearSync() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }

}