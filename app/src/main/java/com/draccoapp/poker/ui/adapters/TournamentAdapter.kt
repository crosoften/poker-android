package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.draccoapp.poker.R
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.ItemTournamentBinding
import com.draccoapp.poker.extensions.viewInvisible
import java.util.Locale

class TournamentAdapter(
    private val onClick: (Tournament) -> Unit
): RecyclerView.Adapter<TournamentAdapter.ViewHolder>() {

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
        private val binding: ItemTournamentBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(tournament: Tournament){

            binding.textTitle.text = tournament.name
//            binding.textDate.text = tournament.date.isoToBrFormat()
            binding.textDate.text = tournament.date

            tournament.distance?.let { distance ->
                when(unit){
                    "metric" -> binding.textDistance.text = buildString {
                        append(String.format(Locale.US, "%.2f", distance))
                        append(binding.root.context.getString(R.string.km_de_voce))
                    }
                    "imperial" -> binding.textDistance.text = buildString {
                        append(String.format(Locale.US, "%.2f", distance))
                        append(binding.root.context.getString(R.string.mi_de_voce))
                    }
                }
            } ?: run {
                binding.textDistance.viewInvisible()
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