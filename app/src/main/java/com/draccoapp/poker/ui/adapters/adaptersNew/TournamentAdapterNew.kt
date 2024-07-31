package com.draccoapp.poker.ui.adapters.adaptersNew

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.databinding.ItemTournamentBinding
import com.draccoapp.poker.extensions.viewInvisible
import com.draccoapp.poker.utils.converterDataNextTournament
import java.util.Locale


class TournamentAdapterNew(
    private val context : Context,
    private val onClick: (NextTournament) -> Unit
): RecyclerView.Adapter<TournamentAdapterNew.ViewHolder>() {

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
        private val binding: ItemTournamentBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(nextTournament: NextTournament){

            binding.textTitle.text = nextTournament.title
            nextTournament.startDatetime?.let {
                binding.textDate.text = converterDataNextTournament(it)
            }

Glide.with(context).load(nextTournament.imageUrl).into(binding.imageView7)


            binding.root.setOnClickListener {
                onClick(nextTournament)
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