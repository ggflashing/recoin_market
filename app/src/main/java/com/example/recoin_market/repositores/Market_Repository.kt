package com.example.recoin_market.repositores

import android.provider.ContactsContract.Data
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.recoin_market.models.UserModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Market_Repository {

    val ussers_data: MutableLiveData<List<UserModels>?> = MutableLiveData<List<UserModels>?>()
    private var databaseReferenceUsers: DatabaseReference = FirebaseDatabase.getInstance().reference.child("user_person")
    fun getUsersList(): LiveData<List<UserModels>?> {
        databaseReferenceUsers.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<UserModels>()
                snapshot.children.forEach {
                    val user = it.getValue(UserModels::class.java)
                    user?.let {
                        tempList.add(it)
                    }
                    if (tempList == null){
                        ussers_data.postValue(null)

                    }else {
                        ussers_data.postValue(tempList)
                    }


                }


            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "List of users getting is failure or aren't registered users yet" + error.message  )


            }
        })

        return ussers_data
    }


}