package com.jonathanlee.wellsafe.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep // For some reason, obfuscated data class will return null in response
data class Stats(
    @SerializedName("location") var location: String,
    @SerializedName("confirmed") var confirmed: Int,
    @SerializedName("deaths") var deaths: Int,
    @SerializedName("recovered") var recovered: Int,
    @SerializedName("active") var active: Int,
) : Serializable
