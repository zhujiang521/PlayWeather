package network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 版权：Zhujiang 个人版权
 * @author zhujiang
 * 版本：1.5
 * 创建日期：2021/4/30
 * 描述：ServiceCreator
 *
 */
object ServiceCreator {

    // 正式版
//    private const val BASE_URL = "https://api.qweather.com/v7/"
    // 开发版
    private const val BASE_URL = "https://devapi.qweather.com/v7/"

    // 城市信息查询
    private const val BASE_CITY_URL = "https://geoapi.qweather.com/v2/"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    private fun create(url: String = BASE_URL): Retrofit {
        // okHttpClientBuilder
        val okHttpClientBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        }

        return RetrofitBuild(
            url = url,
            client = okHttpClientBuilder.build(),
            gsonFactory = GsonConverterFactory.create()
        ).retrofit
    }

    /**
     * get ServiceApi
     */
    fun <T> create(service: Class<T>): T = create().create(service)

    /**
     * get ServiceApi
     */
    fun <T> createCity(service: Class<T>): T = create(BASE_CITY_URL).create(service)

}


class RetrofitBuild(
    url: String, client: OkHttpClient,
    gsonFactory: GsonConverterFactory
) {
    val retrofit: Retrofit = Retrofit.Builder().apply {
        baseUrl(url)
        client(client)
        addConverterFactory(gsonFactory)

    }.build()
}