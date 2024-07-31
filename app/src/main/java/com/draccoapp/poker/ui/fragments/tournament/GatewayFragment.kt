package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.draccoapp.poker.databinding.FragmentGatewayBinding
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class GatewayFragment : Fragment() {

    private var _binding: FragmentGatewayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()


    private val args by navArgs<GatewayFragmentArgs>()
    private val tournament by lazy {
        args.tournament
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGatewayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        setupUI()
        onClick()
    }

    private fun setupUI() {

        Glide.with(requireContext())
            .load(tournament.imageUrl)
            .into(binding.imageView2)

        tournament.title.let {
            binding.textView13.text = it
        }

        tournament.startDatetime.let {
            binding.textView14.text = converterDataNextTournament(it.toString())
        }

        tournament.prize.let {
            binding.textView16.text = it.toString()
        }
    }

    private fun setupObserver() {

    }

    private fun onClick() {

        binding.apply {

            buttonInscrever.setOnClickListener {
//                viewModel.entryTournament(
//                    Entry(
//                        requireContext().getPreferenceData().getUserId(),
//                        tournament.id,
//                        listOf("string")
//                    )
//                )

            }

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}