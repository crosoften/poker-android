package com.draccoapp.poker.ui.fragments.tournament

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentDetailTournamentBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.extensions.viewInvisible
import com.draccoapp.poker.utils.MaskEditUtil
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.converterDistance
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class DetailTournamentFragment : Fragment() {

    private var _binding: FragmentDetailTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()

    private val args by navArgs<DetailTournamentFragmentArgs>()




    private val tournament by lazy {
        args.tournament
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailTournamentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        onClick()
    }




    private fun setupUI() {


//        tournament.eventUrl.let {
//            binding.imageView2.load(it) {
//                crossfade(true)
//                placeholder(R.drawable.ic_wallpaper)
//                error(R.drawable.ic_wallpaper)
//            }
//        }

        if (args.origins == "next"){
            binding.tvAttTour.visibility = View.GONE
            binding.btnReport.visibility = View.GONE
        } else {
            binding.tvAttTour.visibility = View.VISIBLE
            binding.btnReport.visibility = View.VISIBLE
        }

        if (tournament.status == "pending"){
            binding.tvStatus.text = "Inscrito"
            binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_peding)
        } else {
            binding.tvStatus.backgroundTintList = resources.getColorStateList(R.color.status_aproved)
            binding.tvStatus.text = "Validada"
        }


        binding.buttonLink.setOnClickListener {
            if (!tournament.eventUrl.isNullOrEmpty() && tournament.eventUrl!!.contains("https")){
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tournament.eventUrl))
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Link não encontrado", Toast.LENGTH_SHORT).show()
            }

        }

        Glide.with(requireContext())
            .load(tournament.imageUrl)
            .into(binding.imageView2)

        Log.i("ImagemUrl", "setupUI: eventUrl ${tournament.imageUrl}")

        tournament.title.let {
            binding.textView13.text = it
        }

        tournament.startDatetime.let {
            binding.textView14.text = converterDataNextTournament(it.toString())
            binding.textView20.text = buildString {
                append(converterDataNextTournament(it.toString()))
                append(" - ")
                append(tournament.time)
            }
        }

        tournament.finalDatetime.let {
            binding.dateFinish.text = converterDataNextTournament(it.toString())
        }

        tournament.prize.let {
            binding.textView16.text = it.toString()
        }

        tournament.description.let {
            binding.textView18.text = it
        }

        binding.textView22.text = buildString {



            append(tournament.location?.street)
            append(", ")
            append(tournament.location?.number)
            append(", ")
            append(tournament.location?.city)
            append("\n")
            append(getString(R.string.zip_code))
            append(": ")
            append(tournament.location?.zipCode)
            append("\n")
            append(tournament.location?.state)
            append(", ")
            append(tournament.location?.country)


        }
        val distance = tournament.location?.distance
        val type = requireContext().getPreferenceData().getLanguage()
        binding.textView23.text = converterDistance(distance, type)

//        tournament.distance?.let { distance ->
//            when(requireContext().getPreferenceData().getUnit()){
//                "metric" -> binding.textView23.text = buildString {
//                    append(String.format(Locale.US, "%.2f", distance))
//                    append(binding.root.context.getString(R.string.km_de_voce))
//                }
//                "imperial" -> binding.textView23.text = buildString {
//                    append(String.format(Locale.US, "%.2f", distance))
//                    append(binding.root.context.getString(R.string.mi_de_voce))
//                }
//            }
//        } ?: run {
//            binding.textView23.viewInvisible()
//        }



    }

    private fun onClick() {
        binding.apply {

            buttonInscrever.setOnClickListener {
                findNavController()
                    .navigate(
                        DetailTournamentFragmentDirections
                            .actionDetailTournamentFragmentToGatewayFragment(
                                tournament
                            )
                    )
            }

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            btnReport.setOnClickListener {
                findNavController()
                    .navigate(
                        DetailTournamentFragmentDirections
                            .actionDetailTournamentFragmentToTournamentUpdateFragment(args.idSub, args.status!!)
                    )
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}