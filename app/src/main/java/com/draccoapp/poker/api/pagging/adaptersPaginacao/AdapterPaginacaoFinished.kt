package com.draccoapp.poker.api.pagging.adaptersPaginacao


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.contract.Contract
import com.google.android.material.chip.Chip

class AdapterPaginacaoFinished :
    PagingDataAdapter<Contract, AdapterPaginacaoFinished.QuoteViewHolder>(COMPARATOR) {

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeContrato = itemView.findViewById<TextView>(R.id.text_title_list)
        val value = itemView.findViewById<TextView>(R.id.text_value_list)
        val status = itemView.findViewById<Chip>(R.id.chip_list)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null && item.status == "finished") {
            holder.nomeContrato.text = item.title
//            holder.value.text = item.value.toString()
            holder.status.text = item.status
            holder.itemView.visibility = View.VISIBLE
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contract_list, parent, false)
        return QuoteViewHolder(view)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Contract>() {
            override fun areItemsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contract, newItem: Contract): Boolean {
                return oldItem == newItem
            }
        }
    }
}