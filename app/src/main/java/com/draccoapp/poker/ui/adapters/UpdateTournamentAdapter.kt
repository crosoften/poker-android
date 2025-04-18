package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.draccoapp.poker.api.model.response.updateTournament.UpdateTournamentData
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.databinding.ItemTournamentBinding
import com.draccoapp.poker.databinding.ItemUpdateTournamentBinding
import com.draccoapp.poker.utils.converterDataNextTournament

class UpdateTournamentAdapter(
    private val onClick: (UpdateTournamentData) -> Unit
): RecyclerView.Adapter<UpdateTournamentAdapter.ViewHolder>() {

    private var updateList: AsyncListDiffer<UpdateTournamentData> = AsyncListDiffer(this, DiffCallBack)

    fun updateList(list: List<UpdateTournamentData?>){
        updateList.submitList(list)
    }

    object DiffCallBack : DiffUtil.ItemCallback<UpdateTournamentData>() {
        override fun areItemsTheSame(
            oldItem: UpdateTournamentData,
            newItem: UpdateTournamentData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: UpdateTournamentData,
            newItem: UpdateTournamentData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemUpdateTournamentBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val distance = arrayListOf(5, 10, 20, 30, 40, 50)

        fun bind(tournament: UpdateTournamentData){

            binding.textTitleList.text = tournament.title
            binding.textValueList.text = tournament.message
            binding.textDate.text = tournament.date?.let { converterDataNextTournament(it) }
            binding.imageContractList.load(tournament.proofUrl)


            binding.root.setOnClickListener {
                onClick(tournament)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemUpdateTournamentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return updateList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(updateList.currentList[position])
    }
}