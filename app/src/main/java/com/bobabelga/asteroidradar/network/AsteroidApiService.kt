package com.bobabelga.asteroidradar.network

import com.bobabelga.asteroidradar.picofday.PictureOfDay
import com.bobabelga.asteroidradar.util.Constants.BASE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface AsteroidApiService {

    @GET("neo/rest/v1/feed?")
    suspend fun getAsteroidList(@QueryMap map: HashMap<String,String>):
            String

    @GET("planetary/apod?")
    suspend fun getImageOfDay(@Query("api_key") api_key: String):
            PictureOfDay
}

object AsteroidApi {
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}