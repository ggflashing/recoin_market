package com.example.recoin_market.ui.coins_market

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recoin_market.databinding.ItemChatBubleBinding
import com.example.recoin_market.loadImage
import com.example.recoin_market.models.UserModels

class MarketAdapter(var context: Context, var userList: MutableList<UserModels>) :
    RecyclerView.Adapter<MarketAdapter.ViewHolder>() {
    private lateinit var binding: ItemChatBubleBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemChatBubleBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        userList.get(position).let {
            holder.onBind(it)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(binding: ItemChatBubleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(user: UserModels) {
            binding.nickname.text = user.nickname
            binding.message.text = user.message
            binding.image.loadImage (user.avatar)
            binding.buying.text = user.bying.toString();
            binding.sailing.text = user.sales.toString();
            binding.balanceCoins.text = user.coins.toString(); }
    }


}