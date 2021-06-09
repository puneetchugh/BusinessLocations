package com.puneet.vortochallenge.data.network

import com.puneet.vortochallenge.data.api.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkModule {
    companion object {
        val API_END_POINT = "https://api.yelp.com"

        private fun provideCall(): Retrofit {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val original = chain.request()

                    val request = original.newBuilder()
                        .header("Content-Type", "application/json")
                        .header(
                            "Authorization",
                            "Bearer " + "vQc2cEji5MqoewqrQNPWv-qYUDXKqtyY5ZSBRiKeYmFQi6IN0nqCfIiX3b6Zcv10vY9mqX0IlbWfKuj-RH4RY7y4L36BLeC96gG5ZGgZ5IYuKB_O3bgO7QBJT5--YHYx"
                        )
                        .build()

                    val response = chain.proceed(request)
                    response.cacheResponse()
                    response
                }.addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()


            return Retrofit.Builder()
                .baseUrl(API_END_POINT)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun providesNetworkService(): ApiService {
            return provideCall().create(ApiService::class.java)
        }
    }
}