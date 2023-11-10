package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentDetailUpdateBinding
import com.draccoapp.poker.utils.showSnackBar


class DetailUpdateFragment : Fragment() {

    private var _binding: FragmentDetailUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()

    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            btnNewUpdate.setOnClickListener {
                findNavController()
                    .navigate(
                        DetailUpdateFragmentDirections
                            .actionDetailUpdateFragmentToAddUpdateFragment()
                    )

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}