package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.databinding.ItemTournamentBinding

class TournamentAdapter(
    private val onClick: (Tournament) -> Unit
): RecyclerView.Adapter<TournamentAdapter.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<Tournament> = AsyncListDiffer(this, DiffCallBack)

    fun updateList(list: List<Tournament>){
        tournamentList.submitList(list)
    }

    object DiffCallBack : DiffUtil.ItemCallback<Tournament>() {
        override fun areItemsTheSame(
            oldItem: Tournament,
            newItem: Tournament
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Tournament,
            newItem: Tournament
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemTournamentBinding
    ): RecyclerView.ViewHolder(binding.root) {

        private val distance = arrayListOf(5, 10, 20, 30, 40, 50)

        fun bind(tournament: Tournament){

            binding.textTitle.text = tournament.title
            binding.textDate.text = tournament.dataFull
            binding.textDistance.text = buildString {
                append(distance.random())
                append(" km de você")
            }

            binding.root.setOnClickListener {
                onClick(tournament)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTournamentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return tournamentList.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tournamentList.currentList[position])
    }
}