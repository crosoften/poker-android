package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.draccoapp.poker.api.model.response.Tournament
import com.draccoapp.poker.databinding.ItemTournamentListBinding
import com.draccoapp.poker.extensions.isoToBrFormat
import com.draccoapp.poker.extensions.viewInvisible

class TournamentListAdapter(
    private val onClick: (Tournament) -> Unit
): RecyclerView.Adapter<TournamentListAdapter.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<Tournament> = AsyncListDiffer(this, DiffCallBack)

    private var unit = "km"

    fun updateList(list: List<Tournament>){
        tournamentList.submitList(list)
    }

    fun setUnit(unit: String){
        this.unit = unit
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
        private val binding: ItemTournamentListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(tournament: Tournament){

            binding.textTitleList.text = tournament.name
            binding.textDateList.text = tournament.date.isoToBrFormat()

            tournament.imageURL.let { url ->
                binding.imageView5.load(url) {
                    crossfade(true)
                }
            }

            tournament.distance?.let { distance ->
                when(unit){
                    "metric" -> binding.textDistanceList.text = buildString {
                        append(String.format("%.2f", distance))
                        append(" Km de você")
                    }
                    "imperial" -> binding.textDistanceList.text = buildString {
                        append(String.format("%.2f", distance))
                        append(" Mi de você")
                    }
                }
            } ?: run {
                binding.textDistanceList.viewInvisible()
            }

            binding.chipList.text = "null"

            binding.root.setOnClickListener {
                onClick(tournament)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTournamentListBinding.inflate(
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