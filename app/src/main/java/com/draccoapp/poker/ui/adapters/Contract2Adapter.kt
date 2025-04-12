package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.databinding.ItemContractListBinding

class Contract2Adapter(
    private val contracts: List<Contract>,
    private val onClickListener: (Contract) -> Unit
) : RecyclerView.Adapter<Contract2Adapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemContractListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contract: Contract){
            binding.apply {
                textTitleList.text = contract.title
                chipList.text = contract.status
                root.setOnClickListener { onClickListener(contract) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContractListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = contracts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = contracts[position]
        holder.bind(chat)
    }
}