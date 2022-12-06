package network.service

import com.zj.utils.weather.WEATHER_KEY
import model.city.GeoBean
import model.city.TopGeoBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 城市信息查询
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2022/5/19
 * 描述：PlayWeather
 *
 */
interface CityLookupService {

    /**
     * 城市信息查询
     *
     * @param key 用户认证key
     * @param location 需要查询地区的LocationID或以英文逗号分隔的经度,纬度坐标
     * @param lang 多语言设置，默认中文
     *
     * 天气数据是基于地理位置的数据，因此获取天气之前需要先知道具体的位置信息。使用城市搜索，可获取到该城市的基本信息，
     * 包括城市的Location ID（你需要这个ID去查询天气），多语言名称、经纬度、时区、海拔、Rank值、归属上级行政区域、所在行政区域等。
     * 另外，城市搜索也可以帮助你在你的APP中实现模糊搜索，用户只需要输入1-2个字即可获得结果。
     *
     */
    @GET("city/lookup")
    suspend fun getCityLookup(
        @Query("key") key: String = WEATHER_KEY,
        @Query("location") location: String,
        @Query("lang") lang: String
    ): GeoBean


    /**
     * 热门城市查询
     *
     * @param key 用户认证key
     * @param lang 多语言设置，默认中文
     *
     * 天气数据是基于地理位置的数据，因此获取天气之前需要先知道具体的位置信息。使用城市搜索，可获取到该城市的基本信息，
     * 包括城市的Location ID（你需要这个ID去查询天气），多语言名称、经纬度、时区、海拔、Rank值、归属上级行政区域、所在行政区域等。
     * 另外，城市搜索也可以帮助你在你的APP中实现模糊搜索，用户只需要输入1-2个字即可获得结果。
     *
     */
    @GET("city/top")
    suspend fun getCityTop(
        @Query("key") key: String = WEATHER_KEY,
        @Query("lang") lang: String
    ): TopGeoBean

}