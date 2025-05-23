package com.example.recoin_market

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide

fun View.visibility(isVisible: Boolean){
    if(isVisible) { this.visibility = View.VISIBLE}
    else          { this.visibility = View.GONE   }

}

fun ImageView.loadImage(uri: String) {
    Glide
        .with(this)
        .load(uri)
        .centerCrop()
        .into(this)
}

fun showToast(context: Context, msg:String){
    Toast.makeText(context,msg, Toast.LENGTH_LONG).show()
}