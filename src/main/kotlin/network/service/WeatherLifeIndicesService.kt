package network.service

import com.zj.utils.weather.WEATHER_KEY
import model.air.AirNowBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 天气生活指数
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2022/5/19
 * 描述：PlayWeather
 *
 */
interface WeatherLifeIndicesService {

    /**
     * 天气生活指数
     *
     * @param key 用户认证key
     * @param location 需要查询地区的LocationID或以英文逗号分隔的经度,纬度坐标
     * @param lang 多语言设置，默认中文
     *
     * 和风天气生活指数API接口为中国和海外城市提供详细的生活指数实况和预报数据，包括：
        中国天气生活指数：舒适度指数、洗车指数、穿衣指数、感冒指数、运动指数、旅游指数、紫外线指数、空气污染扩散条件指数、
                    空调开启指数、过敏指数、太阳镜指数、化妆指数、晾晒指数、交通指数、钓鱼指数、防晒指数
        海外天气生活指数：运动指数、洗车指数、紫外线指数、钓鱼指数
     *
     */
    @GET("air/now")
    suspend fun getAirNowBean(
        @Query("key") key: String = WEATHER_KEY,
        @Query("location") location: String,
        @Query("lang") lang: String
    ): AirNowBean

}