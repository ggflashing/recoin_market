package com.example.recoin_market.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Char_result(

    @SerializedName("id")
    @Expose
    var id : Int,
    @SerializedName("name")
    @Expose
    var name:String?,
    @SerializedName("status")
    @Expose
    var status: String?,
    @SerializedName("species")
    @Expose
    var species:String?,
    @SerializedName("type")
    @Expose
    var type:String?,
    @SerializedName("gender")
    @Expose
    var gender: String?,
    @SerializedName("image")
    @Expose
    var image:String,

)
