package com.ahmed_apps.dictionary_app.di

import com.ahmed_apps.dictionary_app.data.api.DictionaryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * 定义应用程序的依赖注入模块。
 * 此模块提供网络通信所需的依赖项。
 *
 * 作者：Ahmed Guedmioui
 */
@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    /**
     * 用于记录 HTTP 请求和响应的日志拦截器。
     */
    private val interceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    /**
     * 用于处理 HTTP 请求的 OkHttpClient，并添加了日志拦截器。
     */
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    /**
     * 提供用于网络请求的 DictionaryApi 实例。
     *
     * @return 创建的 DictionaryApi 实例。
     */
    @Provides
    @Singleton
    fun providesDictionaryApi() : DictionaryApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(DictionaryApi.BASE_URL)
            .client(client)
            .build()
            .create()
    }

}
