package com.draccoapp.poker.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.databinding.FragmentTwoFactorBinding
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.CodeValidatedHandler
import com.draccoapp.poker.extensions.showSnackBarRed


class TwoFactorFragment : Fragment() {


    private var _binding: FragmentTwoFactorBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTwoFactorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupUI()
    }

    private fun setupUI() {
        val codeValidatedHandler = CodeValidatedHandler(binding.includeCodeValidated.root, binding.buttonEnter)

        binding.buttonEnter.setOnClickListener {
            val code = codeValidatedHandler.getCodeValidated()
            if (codeValidatedHandler.isAllDigitsEntered()) {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()
            } else {
                binding.root.showSnackBarRed("Por favor, insira todos os dígitos.")
            }
        }

    }

    private fun onclick() {

        binding.apply {

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