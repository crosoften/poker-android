package com.draccoapp.poker.ui.adapters.adaptersNew

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.databinding.ItemTournamentBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.utils.calculateDistance
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.converterDistance


class TournamentAdapterNew(
    private val context: Context,
    location: Location?,
    private val onClick: (NextTournament) -> Unit,
    private val type :String
) : RecyclerView.Adapter<TournamentAdapterNew.ViewHolder>() {

    private var tournamentList: AsyncListDiffer<NextTournament> =
        AsyncListDiffer(this, DiffCallBack)
    private var userLocation: Location? = location

    private var unit = "km"

    fun updateList(list: List<NextTournament>) {
        tournamentList.submitList(list)
    }

    fun updateLocation(location: Location) {
        userLocation = location
        notifyDataSetChanged()
    }

    fun setUnit(unit: String) {
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
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(nextTournament: NextTournament) {

            binding.textTitle.text = nextTournament.title
            nextTournament.startDatetime?.let {
                binding.textDate.text = converterDataNextTournament(it)
            }

            binding.icCamera.visibility = View.INVISIBLE

            Glide.with(context).load(nextTournament.imageUrl).into(binding.imageView7)

            userLocation?.let {
                val latitude = it.latitude
                val longitude = it.longitude

                val distance = calculateDistance(
                    latitude,
                    longitude,
                    nextTournament.location.lat.toDouble(),
                    nextTournament.location.lng.toDouble()
                )

                Log.i("localizacao", "bind: $distance")
            }
            Log.i("localizacao", "torneio: ${nextTournament}")

            binding.textDistance.text = converterDistance(nextTournament.location?.distance, type)

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