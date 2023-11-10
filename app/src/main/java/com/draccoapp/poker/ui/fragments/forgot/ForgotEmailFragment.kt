package com.draccoapp.poker.ui.fragments.forgot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.databinding.FragmentForgotEmailBinding


class ForgotEmailFragment : Fragment() {

    private var _binding: FragmentForgotEmailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotEmailBinding.inflate(inflater, container, false)
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

            buttonEnter.setOnClickListener {
                findNavController()
                    .navigate(
                        ForgotEmailFragmentDirections
                            .actionForgotEmailFragmentToForgotCodeFragment()
                    )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}