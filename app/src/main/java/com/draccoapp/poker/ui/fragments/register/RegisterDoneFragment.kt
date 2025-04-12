package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentRegisterDoneBinding
import com.draccoapp.poker.utils.Preferences


class RegisterDoneFragment : Fragment() {

    private var _binding: FragmentRegisterDoneBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterDoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()

    }

    private fun onclick() {
        val preferences = Preferences(requireContext())
        preferences.setAutorized(false)

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack(R.id.loginFragment, false)
            }

            buttonProfileAbout.setOnClickListener {
                findNavController()
                    .navigate(
                        RegisterDoneFragmentDirections
                            .actionRegisterDoneFragmentToAboutFragment2()
                    )
            }


            buttonProfileContact.setOnClickListener {
                findNavController()
                    .navigate(
                        RegisterDoneFragmentDirections
                            .actionRegisterDoneFragmentToContactFragment2()
                    )
            }

            buttonProfileHelp.setOnClickListener {
                findNavController()
                    .navigate(
                        RegisterDoneFragmentDirections
                            .actionRegisterDoneFragmentToCenterHelpFragment2()
                    )
            }

            buttonProfileTerms.setOnClickListener {
                findNavController()
                    .navigate(
                        RegisterDoneFragmentDirections
                            .actionRegisterDoneFragmentToTermsUseFragment2()
                    )
            }

            buttonProfilePolitic.setOnClickListener {
                findNavController()
                    .navigate(
                        RegisterDoneFragmentDirections
                            .actionRegisterDoneFragmentToPoliticPrivacyFragment2()
                    )
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}