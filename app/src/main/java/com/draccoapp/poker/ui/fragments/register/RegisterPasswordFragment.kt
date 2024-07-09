package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.LocationX
import com.draccoapp.poker.api.model.request.RegisterStep3Body
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentRegisterPasswordBinding
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.utils.Constants.Companion.RegisterCity
import com.draccoapp.poker.utils.Constants.Companion.RegisterCode
import com.draccoapp.poker.utils.Constants.Companion.RegisterConfirmedPassword
import com.draccoapp.poker.utils.Constants.Companion.RegisterCountry
import com.draccoapp.poker.utils.Constants.Companion.RegisterDateBirth
import com.draccoapp.poker.utils.Constants.Companion.RegisterEmail
import com.draccoapp.poker.utils.Constants.Companion.RegisterGender
import com.draccoapp.poker.utils.Constants.Companion.RegisterImageUrl
import com.draccoapp.poker.utils.Constants.Companion.RegisterName
import com.draccoapp.poker.utils.Constants.Companion.RegisterPassword
import com.draccoapp.poker.utils.Constants.Companion.RegisterPhone
import com.draccoapp.poker.utils.Constants.Companion.RegisterState
import com.draccoapp.poker.viewModel.RegisterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegisterPasswordFragment : Fragment() {

    private var _binding: FragmentRegisterPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupObservers()

    }

    private fun setupObservers() {

        viewModel.successRegisterStep3.observe(viewLifecycleOwner) {
            Log.i("RegisterDados", "setupObservers: O id do usuário criado é $it")
            findNavController()
                .navigate(
                    RegisterPasswordFragmentDirections
                        .actionRegisterPasswordFragmentToRegisterDoneFragment()
                )
        }


    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            buttonEnter.setOnClickListener {
                val password = editPassword
                val confirmPassword = editPasswordConfirmed

                //Salvando dados
                RegisterPassword = password.text.toString()
                RegisterConfirmedPassword = confirmPassword.text.toString()

                if (password.text?.length!! < 8) {
                    val stringSenha8digitos = getString(R.string.senha_8digitos)
                    binding.root.showSnackBarRed(stringSenha8digitos)
                } else if (password.text.toString() != confirmPassword.text.toString()) {
                    val stringSenhasDiferentes = getString(R.string.senhas_diferentes)
                    binding.root.showSnackBarRed(stringSenhasDiferentes)
                } else {
                    val registerStep3Body = RegisterStep3Body(
                        birthday = RegisterDateBirth,
                        code = RegisterCode,
                        gender = RegisterGender,
                        location = LocationX(
                            city = RegisterCity,
                            state = RegisterState
                        ),
                        password = RegisterPassword,
                        passwordConfirmation = RegisterConfirmedPassword,
                        email = RegisterEmail,
                        imageUrl = RegisterImageUrl,
                        name = RegisterName,
                        phone = RegisterPhone,
                    )

                    viewModel.registerStep3(registerStep3Body)
                }


            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}