package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentDetailTournamentBinding
import com.draccoapp.poker.extensions.getPreferenceData
import com.draccoapp.poker.extensions.viewInvisible
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

        tournament.imageURL.let {
            binding.imageView2.load(it) {
                crossfade(true)
                placeholder(R.drawable.ic_wallpaper)
                error(R.drawable.ic_wallpaper)
            }
        }

        tournament.name.let {
            binding.textView13.text = it
        }

        tournament.date.let {
            binding.textView14.text = it
            binding.textView20.text = it
        }

        tournament.prize.let {
            binding.textView16.text = it.toString()
        }

        tournament.description.let {
            binding.textView18.text = it
        }

        binding.textView22.text = buildString {
            append(tournament.latitude)
            append(",")
            append(tournament.longitude)

        }

        tournament.distance?.let { distance ->
            when(requireContext().getPreferenceData().getUnit()){
                "metric" -> binding.textView23.text = buildString {
                    append(String.format(Locale.US, "%.2f", distance))
                    append(binding.root.context.getString(R.string.km_de_voce))
                }
                "imperial" -> binding.textView23.text = buildString {
                    append(String.format(Locale.US, "%.2f", distance))
                    append(binding.root.context.getString(R.string.mi_de_voce))
                }
            }
        } ?: run {
            binding.textView23.viewInvisible()
        }



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
                            .actionDetailTournamentFragmentToTournamentUpdateFragment()
                    )
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}