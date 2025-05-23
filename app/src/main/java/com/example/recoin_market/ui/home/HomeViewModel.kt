package com.example.recoin_market.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recoin_market.models.Char_result
import com.example.recoin_market.repositores.Char_Repositorys

class HomeViewModel : ViewModel() {

    var repository: Char_Repositorys = Char_Repositorys() // обьявление Char_Repository()

    var liveData: LiveData<List<Char_result>?> = MutableLiveData<List<Char_result>?>() // Бассейн данных ? придут ли данные

    fun getResponseLiveData(): LiveData<List<Char_result>?> {
        liveData = repository.getCharactersList()
        return liveData
    }
}