package com.example.recoin_market.models

data class UserModels(

    val nickname: String,
    val email:String,
    val password: String,
    val confirmPassword: String,
    val avatar: String,
    var coins: Int,
    var bying: Int,
    var sales: Int,
    var message:String

): java.io.Serializable

{
    constructor() : this ("","","",
        "","",
        0,0,0,"Good day")

}
