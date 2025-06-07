package com.example.recoin_market.models

import com.google.android.material.color.utilities.Score

data class PDFModel(

    var id: String? = null,
    var fileTitle: String,
    var downloadUrl:String,
    var idUser:String,
    var score:Int

): java.io.Serializable
{
    constructor(): this (null, "","","",0)
}
