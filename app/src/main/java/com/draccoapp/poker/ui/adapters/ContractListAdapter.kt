package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.data.Contract
import com.draccoapp.poker.databinding.ItemContractListBinding

class ContractListAdapter(
    private val onClick: (Contract) -> Unit
): RecyclerView.Adapter<ContractListAdapter.ViewHolder>() {

    private var contractList: AsyncListDiffer<Contract> = AsyncListDiffer(this, DiffCallBack)

    fun updateList(list: List<Contract>){
        contractList.submitList(list)
    }

    object DiffCallBack : DiffUtil.ItemCallback<Contract>() {
        override fun areItemsTheSame(
            oldItem: Contract,
            newItem: Contract
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Contract,
            newItem: Contract
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemContractListBinding
    ): RecyclerView.ViewHolder(binding.root) {


        fun bind(contract: Contract){

            binding.textTitleList.text = contract.title
            binding.textValueList.text = buildString {
                append("Valor: ")
                append("\$ 100000,00")
            }

            binding.root.setOnClickListener {
                onClick(contract)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemContractListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return contractList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contractList.currentList[position])
    }
}