package com.example.recoin_market.repositores

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recoin_market.models.Char_result
import com.example.recoin_market.remote_data.Char_Apiservice
import com.example.recoin_market.remote_data.RetrofitB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Char_Repositorys {

    companion object{ // констант конструктора вызывает один раз и использует много раз
        private  lateinit var apiservice: Char_Apiservice
        init {   // Вызывается когда человек хочет с помощью by lazy
            try {
                apiservice = RetrofitB.retrofitService
            }catch (e:Exception){
                Log.e("TAG","ERROR!!!"+ e.localizedMessage)

            }
        }


    }


    val data: MutableLiveData <List<Char_result>?> = MutableLiveData<List<Char_result>?>()

    fun getCharactersList(): LiveData<List<Char_result>?>{
        val apiCall: Call<List<Char_result>> = apiservice.getAllData()
        apiCall.enqueue(object : Callback <List<Char_result>?> {
            override fun onResponse(call: Call<List<Char_result>?>, response: Response<List<Char_result>?>) {
                try {
                    data.postValue(response.body())
                } catch (e: Exception) {
                    Log.e("NNNN", "ERROR!!  SALIH  NO DATA!!!" + e.localizedMessage)
                }

            }

            override fun onFailure(call: Call<List<Char_result>?>, throw_exeption: Throwable) {
                Log.e("LOL", "NO DATA" + throw_exeption.localizedMessage)
                data.postValue(null)
            }
        })
        return data
    }



}