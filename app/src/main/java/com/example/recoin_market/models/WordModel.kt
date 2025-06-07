package com.example.recoin_market.models

data class WordModel(

    var id: String? = null,
    val fileWordTitle: String,
    val downloadWordUrl:String,
    val idUser:String,
    var score:Int

): java.io.Serializable
{
    constructor(): this (null, "","","",0)
}


