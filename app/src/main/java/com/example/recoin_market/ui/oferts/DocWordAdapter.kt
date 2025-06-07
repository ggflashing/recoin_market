package com.example.recoin_market.ui.oferts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recoin_market.R
import com.example.recoin_market.databinding.ItemScenarioWordBinding
import com.example.recoin_market.models.WordModel


class DocWordAdapter : ListAdapter<WordModel,DocWordAdapter.ViewHolder>(Callback()) {
    val onItemClik: ((WordModel) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocWordAdapter.ViewHolder {

        val binding =
            ItemScenarioWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocWordAdapter.ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class ViewHolder(private val binding: ItemScenarioWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: WordModel?) {
            if (item != null){
                binding.titleOfScenario.text = item.fileWordTitle
                binding.urlScenario.text = item.downloadWordUrl
                binding.imageScenario.setImageResource(R.drawable.search_word_02)
                binding.counterScore.text = item.score.toString()

            }else {
                binding.titleOfScenario.text = "File's title"
                itemView.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = getItem(position)
                        onItemClik?.invoke(item)
                        val bundle = Bundle()
                        bundle.putSerializable("key_wordUrl", item as WordModel)
                        bundle.putString("firebase_key_word", item.id)
                        bundle.putString("author_userID", item.idUser)
                        itemView.findNavController().navigate(R.id.action_cabinetFragment_to_wordFragment)

                    }
                }
            }


        }


    }
}



    //Это cравнение элементов нужно для: способа обновления данных в ListAdapter.
    // ListAdapter эффективно вычисляет разницу между старым и новым списками
// и обновляет RecyclerView соответствующим образом.

    class  Callback: DiffUtil.ItemCallback <WordModel> () {
        override fun areItemsTheSame(oldItem: WordModel, newItem: WordModel): Boolean {

            return oldItem.downloadWordUrl == newItem.downloadWordUrl
        }

        override fun areContentsTheSame(oldItem: WordModel, newItem: WordModel): Boolean {
            return  oldItem == newItem
        }
    }

