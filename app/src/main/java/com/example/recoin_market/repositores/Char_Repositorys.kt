package com.example.recoin_market.repositores

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recoin_market.models.Char_result
import com.example.recoin_market.remote_data.RetrofitB
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Char_Repositorys {

    companion object{
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
                    data.postValue(response.body()) // postValue сажает данные с response.body в data хранилища данные
                } catch (e: Exception) {
                    Log.e("TAG", "ERROR!! NO DATA!!!")
                }

            }

            override fun onFailure(call: Call<List<Char_result>?>, throw_exeption: Throwable) {
                Log.e("TAG", "NO DATA" + throw_exeption.localizedMessage)
                data.postValue(null)
            }
        })
        return data
    }



}