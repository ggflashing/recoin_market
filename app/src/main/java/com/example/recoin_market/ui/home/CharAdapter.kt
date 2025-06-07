package com.example.recoin_market.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recoin_market.databinding.ItemCardBinding
import com.example.recoin_market.loadImage
import com.example.recoin_market.models.Char_result

class CharAdapter (var context: Context, var list: List<Char_result?>?)
    :RecyclerView.Adapter<CharAdapter.ViewHolder>(){

private lateinit var binding: ItemCardBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharAdapter.ViewHolder, position: Int) {
        list?.get(position)?.let {
            holder.onBind(it)
        }
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ViewHolder (binding: ItemCardBinding)
        :RecyclerView.ViewHolder(binding.root) {



        fun onBind(charResult: Char_result) {
            binding.apply {
                cardName.text = charResult.name
                cardImage.loadImage(charResult.image)
                cardSpecies.text = charResult.species
                cardStatus.text = charResult.status
                cardGender.text = charResult.gender
            }

            if (charResult.type == "") {
                binding.cardType.text = "Uknown"

            }else {
                binding.cardType.text = charResult.type
            }


        }
    }

}



