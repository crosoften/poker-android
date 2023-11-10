package com.draccoapp.poker.ui.fragments.register

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentRegisterCodeBinding
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.CodeValidatedHandler
import com.draccoapp.poker.utils.showSnackBarRed


class RegisterCodeFragment : Fragment() {

    private var _binding: FragmentRegisterCodeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupUI()
    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

        }
    }

    private fun setupUI() {
        val codeValidatedHandler = CodeValidatedHandler(binding.includeCodeValidated.root, binding.buttonEnter)

        binding.buttonEnter.setOnClickListener {
            val code = codeValidatedHandler.getCodeValidated()
            if (codeValidatedHandler.isAllDigitsEntered()) {
                findNavController()
                    .navigate(
                        RegisterCodeFragmentDirections
                            .actionRegisterCodeFragmentToRegisterPasswordFragment()
                    )
            } else {
                binding.root.showSnackBarRed("Por favor, insira todos os dígitos.")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}