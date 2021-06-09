package com.jonathanlee.wellsafe.data.source.remote

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StatsApi {
    private var retrofit: Retrofit? = null
    private val gson = GsonBuilder().setLenient().create()
    private const val BASE_URL = "https://covid2019-api.herokuapp.com/"


    val client: Retrofit get() {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build()
        }
        return retrofit!!
    }
}