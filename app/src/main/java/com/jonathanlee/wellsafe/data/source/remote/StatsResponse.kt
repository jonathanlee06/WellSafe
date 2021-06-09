package com.jonathanlee.wellsafe.data.source.remote

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jonathanlee.wellsafe.data.model.Stats
import java.io.Serializable

data class StatsResponse (
    @SerializedName("data")
    @Expose
    var data: Stats,
    @SerializedName("dt")
    @Expose
    var dt: String,
    @SerializedName("ts")
    @Expose
    var ts: Int,
) : Serializable