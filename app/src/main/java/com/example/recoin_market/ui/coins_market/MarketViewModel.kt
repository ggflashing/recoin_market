package com.example.recoin_market.ui.coins_market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recoin_market.models.UserModels
import com.example.recoin_market.repositores.Market_Repository

class MarketViewModel:ViewModel() {

    var repository: Market_Repository = Market_Repository()

    var liveData: LiveData<List<UserModels>?> = MutableLiveData<List<UserModels>?>()
    fun getResponseLiveData(): LiveData<List<UserModels>?> {
        liveData = repository.getUsersList()
        return liveData
    }
}