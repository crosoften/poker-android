package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.RegisterStep2Body
import com.draccoapp.poker.databinding.FragmentRegisterCodeBinding
import com.draccoapp.poker.utils.CodeValidatedHandler
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.ui.fragments.login.LoginFragmentDirections
import com.draccoapp.poker.utils.Constants.Companion.RegisterCode
import com.draccoapp.poker.utils.Constants.Companion.RegisterEmail
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterCodeFragment : Fragment() {

    private var _binding: FragmentRegisterCodeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModel()
    private var firstTimeMovingToDoneFragment = true


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
        setupObservers()
    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

        }
    }

    private fun setupObservers() {
        viewModel.successRegisterStep2.observe(viewLifecycleOwner) {

            if (firstTimeMovingToDoneFragment) {
                firstTimeMovingToDoneFragment = false
                findNavController()
                    .navigate(
                        RegisterCodeFragmentDirections
                            .actionRegisterCodeFragmentToRegisterPasswordFragment()
                    )
                Log.i("Token", "setupObservers: O token foi ${it.message}")
            }

        }
    }

    private fun setupUI() {
        val codeValidatedHandler = CodeValidatedHandler(binding.includeCodeValidated.root, binding.buttonEnter)

        binding.buttonEnter.setOnClickListener {
            val code = codeValidatedHandler.getCodeValidated()
            firstTimeMovingToDoneFragment = true

            if (code.length < 4) {
                val stringCode = getString(R.string.code_invalido)
                binding.root.showSnackBarRed(stringCode)
            } else {
                RegisterCode = code
                viewModel.registerStep2(RegisterStep2Body(code = code, email = RegisterEmail))
            }


            Log.i("Token", "setupUI: O código digitado foi $code")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}