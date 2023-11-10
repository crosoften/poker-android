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
import com.draccoapp.poker.databinding.FragmentLoginBinding
import com.draccoapp.poker.databinding.FragmentSplashBinding
import com.draccoapp.poker.ui.activities.MainActivity


class LoginFragment : Fragment() {


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

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

        onClick()
        setupUI()
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
                findNavController()
                    .navigate(
                        LoginFragmentDirections
                            .actionLoginFragmentToTwoFactorFragment()
                    )

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


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}