package com.draccoapp.poker.ui.fragments.login

import android.content.Intent
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
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.Validation
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: AuthViewModel by viewModel()

    private val TAG = "LoginFragment"
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

//        viewModel.login.observe(viewLifecycleOwner) { response ->
//            response?.let {
//                if(response.role == "user"){
//                    startActivity(Intent(requireContext(), MainActivity::class.java))
//                    requireActivity().finishAffinity()
//                } else {
//                    binding.root.showSnackBarRed("Usuário não autorizado")
//                }
//            }
//        }
//
//        viewModel.error.observe(viewLifecycleOwner) { error ->
//            error?.let {
//                binding.root.showSnackBarRed(it)
//            }
//        }
    }

    private fun setupUI() {
        val text = "Ainda não tem cadastro?  \nClique aqui!"
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

                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()

//                if(validateOfFields()){
//                    viewModel.login(
//                        Login(
//                            email = email,
//                            password = password
//                        )
//                    )
//                }

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

        if(email.isEmpty()){
            binding.editEmail.error = "Campo obrigatório"
            return false
        } else {
            binding.editEmail.error = null
        }

        if(Validation.isEmailValid(email).not()){
            binding.editEmail.error = "E-mail inválido"
            return false
        } else {
            binding.editEmail.error = null
        }

        if(password.isEmpty()){
            binding.editPassword.error = "Campo obrigatório"
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