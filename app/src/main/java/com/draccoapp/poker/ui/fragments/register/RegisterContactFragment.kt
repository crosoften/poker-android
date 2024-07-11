package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.RegisterStep1Body
import com.draccoapp.poker.databinding.FragmentRegisterContactBinding
import com.draccoapp.poker.utils.Constants.Companion.RegisterEmail
import com.draccoapp.poker.utils.Constants.Companion.RegisterPhone
import com.draccoapp.poker.utils.MaskEditUtil
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterContactFragment : Fragment() {

    private var _binding: FragmentRegisterContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModel()
    private var firstTimeMovingToDoneFragment = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupObserves()
    }

    private fun setupObserves() {
        viewModel.successRegisterStep1.observe(viewLifecycleOwner) {
//            mostrarToast("Código enviar para o email", requireContext())

            if (firstTimeMovingToDoneFragment) {
                firstTimeMovingToDoneFragment = false
                findNavController()
                    .navigate(
                        RegisterContactFragmentDirections
                            .actionRegisterContactFragmentToRegisterCodeFragment()
                    )
            }


        }

    }


    private fun onclick() {
        binding.editPhone.addTextChangedListener(MaskEditUtil.mask(binding.editPhone, MaskEditUtil.FORMAT_PHONE))

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            buttonEnter.setOnClickListener {
                firstTimeMovingToDoneFragment = true
                val email = editEmail
                val phone = editPhone
                val phoneLimpo = MaskEditUtil.unmask(phone.text.toString())

                //Salvando dados
                RegisterEmail = email.text.toString()
                RegisterPhone = phoneLimpo

                val allFieldsFilled = validateEditTexts(email, phone)
                if (!allFieldsFilled) {
                    val stringPreencherCampos = getString(R.string.campos_necessarios)
                    mostrarToast(stringPreencherCampos, requireContext())
                } else if (phoneLimpo.length < 10) {
                    val stringPhoneInvalido = getString(R.string.phone_invalido)
                    mostrarToast(stringPhoneInvalido, requireContext())
                } else {
                    viewModel.registerStep1(RegisterStep1Body(email = email.text.toString(), phone = phoneLimpo))
                }

//                findNavController()
//                    .navigate(
//                        RegisterContactFragmentDirections
//                            .actionRegisterContactFragmentToRegisterCodeFragment()
//                    )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}