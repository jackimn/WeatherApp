package com.example.walkingpark.di.module

import android.content.Context
import android.location.Geocoder
import com.example.walkingpark.constants.Common
import com.example.walkingpark.data.api.PublicApiService
import com.example.walkingpark.data.api.UnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
object PublicDataApiModule {

/*
    // TODO RestApi TimeOut 관련.
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
*/

    @AirAPI
    @Provides
    fun provideDataFromAirApi(): PublicApiService {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Common.BASE_URL_API_AIR)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.unsafeOkHttpClient().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        val api: PublicApiService by lazy {
            retrofit.create(PublicApiService::class.java)
        }
        return api
    }

    @StationAPI
    @Provides
    fun provideDataFromStationApi(): PublicApiService {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Common.BASE_URL_API_STATION)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.unsafeOkHttpClient().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //.client(okHttpClient)
                .build()
        }

        val api: PublicApiService by lazy {
            retrofit.create(PublicApiService::class.java)
        }

        return api
    }

    @WeatherApi
    @Provides
    fun provideDataFromWeatherApi(): PublicApiService {

        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(Common.BASE_URL_API_WEATHER)
                .addConverterFactory(GsonConverterFactory.create())
                .client(UnsafeOkHttpClient.unsafeOkHttpClient().build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //     .client(okHttpClient)
                .build()
        }

        val api: PublicApiService by lazy {
            retrofit.create(PublicApiService::class.java)
        }

        return api
    }

    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AirAPI

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class StationAPI

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class WeatherApi

}