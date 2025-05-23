package com.example.recoin_market.repositores

import com.example.recoin_market.models.Char_result
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Char_Apiservice {

    private val list_id : List<String> get() =  (1 .. 240).map {
        it.toString()
    }

@GET(value = "/api/character/{ids}")
fun getAllData(
    @Path("ids")
    ids:String = list_id.joinToString { "," }

): Call<List<Char_result>>

@GET("/api/character/60")
fun getCharById60(): Call<Char_result>

}