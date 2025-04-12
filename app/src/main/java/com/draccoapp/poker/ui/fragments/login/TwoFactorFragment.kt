package com.draccoapp.poker.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.Login2FARequest
import com.draccoapp.poker.databinding.FragmentTwoFactorBinding
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.CodeValidatedHandler
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TwoFactorFragment : Fragment() {


    private var _binding: FragmentTwoFactorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthViewModel>()


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

        setupObserver()
        onclick()
        setupUI()
    }

    private fun setupObserver() {
        viewModel.login2fa.observe(viewLifecycleOwner) { response ->
            response?.let {
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { response ->
            response?.let {
                binding.root.showSnackBarRed(it)
            }
        }
    }

    private fun setupUI() {
        val codeValidatedHandler = CodeValidatedHandler(binding.includeCodeValidated.root, binding.buttonEnter)

        binding.buttonEnter.setOnClickListener {
            val code = codeValidatedHandler.getCodeValidated()
            if (codeValidatedHandler.isAllDigitsEntered()) {
                viewModel.login2fa(
                    Login2FARequest(
                        key = viewModel.getKey(),
                        code = code
                    )
                )
            } else {
                binding.root.showSnackBarRed(getString(R.string.por_favor_insira_todos_os_d_gitos))
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