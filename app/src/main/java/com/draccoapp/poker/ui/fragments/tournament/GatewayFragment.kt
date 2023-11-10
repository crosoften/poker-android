package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentDetailTournamentBinding
import com.draccoapp.poker.databinding.FragmentGatewayBinding


class GatewayFragment : Fragment() {


    private var _binding: FragmentGatewayBinding? = null
    private val binding get() = _binding!!

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

        onClick()
    }

    private fun onClick() {

        binding.apply {

            buttonInscrever.setOnClickListener {
                findNavController()
                    .navigate(
                        GatewayFragmentDirections
                            .actionGatewayFragmentToSubscribeTournamentFragment()
                    )
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