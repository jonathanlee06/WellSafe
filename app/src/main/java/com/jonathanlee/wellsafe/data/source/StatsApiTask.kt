package com.jonathanlee.wellsafe.data.source

import com.jonathanlee.wellsafe.data.model.Stats
import com.jonathanlee.wellsafe.data.source.remote.StatsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StatsApiTask {

    @GET("v2/country/malaysia")
    fun getMalaysiaData(): Call<StatsResponse?>
}