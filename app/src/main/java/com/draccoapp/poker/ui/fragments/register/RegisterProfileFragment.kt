package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentRegisterProfileBinding


class RegisterProfileFragment : Fragment() {

    private var _binding: FragmentRegisterProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupUI()

    }

    private fun setupUI() {
        val termsOfUse = "Termos de uso"
        val politicOfPrivacy = "Políticas de privacidade"
        val text = buildString {
            append("Declaro que concordo e aceito os ")
            append(termsOfUse)
            append(" e ")
            append(politicOfPrivacy)
        }

        val spannableStringBuilder = SpannableStringBuilder(text)

        val clickableSpanTerms = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController()
                    .navigate(
                        RegisterProfileFragmentDirections
                            .actionRegisterProfileFragmentToTermsUseFragment2()
                    )
            }
        }

        val clickableSpanPolitic = object : ClickableSpan() {
            override fun onClick(widget: View) {
                findNavController()
                    .navigate(
                        RegisterProfileFragmentDirections
                            .actionRegisterProfileFragmentToPoliticPrivacyFragment2()
                    )
            }
        }

        val startTerms = text.indexOf(termsOfUse)
        val endTerms = startTerms + termsOfUse.length

        val startPolitic = text.indexOf(politicOfPrivacy)
        val endPolitic = startPolitic + politicOfPrivacy.length

        spannableStringBuilder.setSpan(clickableSpanTerms, startTerms, endTerms, 0)
        spannableStringBuilder.setSpan(clickableSpanPolitic, startPolitic, endPolitic, 0)

        binding.checkBox.text = spannableStringBuilder
        binding.checkBox.movementMethod = LinkMovementMethod.getInstance()

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
                        RegisterProfileFragmentDirections
                            .actionRegisterProfileFragmentToRegisterContactFragment()
                    )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}