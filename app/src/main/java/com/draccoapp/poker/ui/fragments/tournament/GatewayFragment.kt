package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.draccoapp.poker.databinding.FragmentGatewayBinding


class GatewayFragment : Fragment() {


    private var _binding: FragmentGatewayBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: TournamentViewModel by viewModel()

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

        tournament.imageURL.let {
            binding.imageView2.load(it) {
                crossfade(true)
            }
        }

        tournament.name.let {
            binding.textView13.text = it
        }

        tournament.date.let {
            binding.textView14.text = it
        }

        tournament.prize.let {
            binding.textView16.text = it.toString()
        }
    }

    private fun setupObserver() {

//        viewModel.entry.observe(viewLifecycleOwner) {
//            findNavController()
//                .navigate(
//                    GatewayFragmentDirections
//                        .actionGatewayFragmentToSubscribeTournamentFragment()
//                )
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) {
//            binding.root.showSnackBarRed(it)
//        }
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