package com.example.recoin_market.ui.oferts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recoin_market.R
import com.example.recoin_market.databinding.ItemScenarioBinding
import com.example.recoin_market.models.PDFModel
import retrofit2.Callback

class DocPDFAdapter : ListAdapter<PDFModel, DocPDFAdapter.DocViewHolder>(Callback()) {
    var onItemClick: ((PDFModel) -> Unit)? = null



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocViewHolder {
        val binding =
            ItemScenarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DocViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class DocViewHolder(private val binding: ItemScenarioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: PDFModel?) {
            if (item != null) {
                binding.titleOfScenario.text= item.fileTitle
                binding.urlScenario.text= item.downloadUrl
                binding . imageScenario.setImageResource (R.drawable.pdf_icon4)
                binding.counterScore.text = item.score.toString()
            } else {
                binding.titleOfScenario.text = "File's title"
            }
            itemView.setOnClickListener {
                val position = bindingAdapterPosition

                if (position != RecyclerView.NO_POSITION) {
                val item = getItem(position)
                onItemClick ?. invoke (item)
                val bundle = Bundle()
                bundle.putSerializable(
                    "key_pdfUrl",
                    item as PDFModel // item хранит позицию и Serializable хранит свернутную целую модельку PDFModel к 45
                )
                    bundle.putString ("firebase_key_pdf", item.id)
                itemView.findNavController()
                    .navigate(R.id.action_cabinetFragment_to_PDFFragment, bundle)
            }
            }
        }
    }

    class Callback : DiffUtil.ItemCallback<PDFModel>() {
        override fun areItemsTheSame(oldItem: PDFModel, newItem: PDFModel): Boolean {
            return oldItem.downloadUrl == newItem.downloadUrl //сравнивает один с другим uri файлов
        }

        override fun areContentsTheSame(oldItem: PDFModel, newItem: PDFModel): Boolean {
            return oldItem == newItem
        }
    }

}