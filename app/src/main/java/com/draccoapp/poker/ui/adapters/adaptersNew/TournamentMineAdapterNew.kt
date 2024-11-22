package com.draccoapp.poker.ui.adapters.adaptersNew

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.Tournament
import com.draccoapp.poker.api.model.response.tournamentInIm.TournamentInImData
import com.draccoapp.poker.databinding.ItemTournamentBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.converterDistance


class TournamentMineAdapterNew(
    private val context : Context,
    private val onClick: (TournamentInImData) -> Unit,
    private val onClickUpdate: (TournamentInImData) -> Unit
): RecyclerView.Adapter<TournamentMineAdapterNew.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<TournamentInImData> = AsyncListDiffer(this, DiffCallBack)

    private var unit = "km"

    fun updateList(list: List<TournamentInImData>){
        tournamentList.submitList(list)
    }

    fun setUnit(unit: String){
        this.unit = unit
    }

    object DiffCallBack : DiffUtil.ItemCallback<TournamentInImData>() {
        override fun areItemsTheSame(
            oldItem: TournamentInImData,
            newItem: TournamentInImData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: TournamentInImData,
            newItem: TournamentInImData
        ): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: ItemTournamentBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(nextTournament: TournamentInImData){

            binding.textTitle.text = nextTournament.tournament?.title
            nextTournament.tournament?.startDatetime.let {
                binding.textDate.text = it?.let { it1 -> converterDataNextTournament(it1) }
            }

            Glide.with(context).load(nextTournament.tournament?.imageUrl)
                .placeholder(R.drawable.img_placeholder_poker)
                .into(binding.imageView7)

            binding.root.setOnClickListener {
                onClick(nextTournament)
            }

            val type = context.getPreferenceData().getLanguage()
            binding.textDistance.text = converterDistance(nextTournament.tournament?.location?.distance, type)

            binding.icCamera.setOnClickListener {
                onClickUpdate(nextTournament)
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