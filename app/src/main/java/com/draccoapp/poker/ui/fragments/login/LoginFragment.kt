package com.draccoapp.poker.ui.fragments.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.databinding.FragmentLoginBinding
import com.draccoapp.poker.utils.Validation
import com.draccoapp.poker.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthViewModel>()

    private var firstTimeMovingToDoneFragment = true
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        onClick()
        setupUI()

    }

    private fun setupObserver() {

        viewModel.login.observe(viewLifecycleOwner) { response ->
            response?.let {
                findNavController()
                    .navigate(
                        LoginFragmentDirections
                            .actionLoginFragmentToTwoFactorFragment()
                    )
            }

        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (firstTimeMovingToDoneFragment){
                    firstTimeMovingToDoneFragment = false
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterDoneFragment())
                }
            }
        }
    }

    private fun setupUI() {
        val text = getString(R.string.ainda_n_o_tem_cadastro_clique_aqui)
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)),
            25,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.labelCreate.text = spannableString
    }

    private fun onClick() {

        binding.apply {
            buttonEnter.setOnClickListener {

                if (validateOfFields()) {
                    viewModel.login(
                        Login(
                            credential = email,
                            password = password
                        )
                    )
                }
                firstTimeMovingToDoneFragment = true
            }

            labelForgot.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections
                        .actionLoginFragmentToForgotEmailFragment()
                )
            }

            labelCreate.setOnClickListener {
                findNavController()
                    .navigate(
                        LoginFragmentDirections
                            .actionLoginFragmentToRegisterProfileFragment()
                    )
            }
        }

    }

    private fun validateOfFields(): Boolean {

        email = binding.editEmail.text.toString()
        password = binding.editPassword.text.toString()

        if (email.isEmpty()) {
            binding.editEmail.error = getString(R.string.campo_obrigat_rio)
            return false
        } else {
            binding.editEmail.error = null
        }

        if (Validation.isEmailValid(email).not()) {
            binding.editEmail.error = getString(R.string.e_mail_inv_lido)
            return false
        } else {
            binding.editEmail.error = null
        }

        if (password.isEmpty()) {
            binding.editPassword.error = getString(R.string.campo_obrigat_rio)
            return false
        } else {
            binding.editPassword.error = null
        }

        return true

    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}