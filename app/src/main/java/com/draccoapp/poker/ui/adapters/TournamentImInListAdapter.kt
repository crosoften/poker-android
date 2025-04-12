package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.model.response.homeFrament.TournamentsImIn
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.ItemTournamentListBinding
import com.draccoapp.poker.extensions.viewInvisible
import com.draccoapp.poker.utils.converterDataNextTournament
import java.util.Locale

class TournamentImInListAdapter(
    private val onClick: (TournamentsImIn) -> Unit
): RecyclerView.Adapter<TournamentImInListAdapter.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<TournamentsImIn> = AsyncListDiffer(this, DiffCallBack)

    private var unit = "km"

    fun updateList(list: List<TournamentsImIn>){
        tournamentList.submitList(list)
    }

    fun setUnit(unit: String){
        this.unit = unit
    }

    object DiffCallBack : DiffUtil.ItemCallback<TournamentsImIn>() {
        override fun areItemsTheSame(
            oldItem: TournamentsImIn,
            newItem: TournamentsImIn
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TournamentsImIn,
            newItem: TournamentsImIn
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemTournamentListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(tournament: TournamentsImIn){

            binding.textTitleList.text = tournament.tournament.title
            binding.textDateList.text = tournament.tournament.startDatetime?.let {
                converterDataNextTournament(
                    it
                )
            }

            tournament.tournament.imageUrl.let { url ->
                binding.imageView5.load(url) {
                    crossfade(true)
                    placeholder(R.drawable.ic_wallpaper)
                    error(R.drawable.ic_wallpaper)
                }
            }

//            tournament.distance?.let { distance ->
//                when(unit){
//                    "metric" -> binding.textDistanceList.text = buildString {
//                        append(String.format(Locale.US, "%.2f", distance))
//                        append(binding.root.context.getString(R.string.km_de_voce))
//                    }
//                    "imperial" -> binding.textDistanceList.text = buildString {
//                        append(String.format(Locale.US, "%.2f", distance))
//                        append(binding.root.context.getString(R.string.mi_de_voce))
//                    }
//                }
//            } ?: run {
                binding.textDistanceList.viewInvisible()
//            }


            if (tournament.status == "pending"){
                binding.chipList.text = "Inscrito"
            } else {
                binding.chipList.text = "Validada"
            }

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