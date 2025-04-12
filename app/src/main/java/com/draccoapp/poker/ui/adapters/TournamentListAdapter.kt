package com.draccoapp.poker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.ItemTournamentListBinding
import com.draccoapp.poker.extensions.viewInvisible
import com.draccoapp.poker.utils.converterDataNextTournament
import java.util.Locale

class TournamentListAdapter(
    private val onClick: (NextTournament) -> Unit
): RecyclerView.Adapter<TournamentListAdapter.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<NextTournament> = AsyncListDiffer(this, DiffCallBack)

    private var unit = "km"

    fun updateList(list: List<NextTournament>){
        tournamentList.submitList(list)
    }

    fun setUnit(unit: String){
        this.unit = unit
    }

    object DiffCallBack : DiffUtil.ItemCallback<NextTournament>() {
        override fun areItemsTheSame(
            oldItem: NextTournament,
            newItem: NextTournament
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NextTournament,
            newItem: NextTournament
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemTournamentListBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(tournament: NextTournament){

            binding.textTitleList.text = tournament.title
            binding.textDateList.text = tournament.startDatetime?.let {
                converterDataNextTournament(
                    it
                )
            }

            tournament.imageUrl.let { url ->
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