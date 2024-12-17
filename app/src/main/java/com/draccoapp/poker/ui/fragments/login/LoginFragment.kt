package com.draccoapp.poker.ui.fragments.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.ChatMessageSend
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.Login2faBodyNew
import com.draccoapp.poker.api.service.chatSocket.ChatSocketService
import com.draccoapp.poker.databinding.FragmentLoginBinding
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.Constants
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.Validation
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.AuthViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

private const val TAG = "LoginFragment"

class LoginFragment : Fragment() {

    private val socket: ChatSocketService by inject()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<AuthViewModel>()

    private var firstTimeMovingToDoneFragment = true
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun setupObserver() {

        viewModel.successLogin.observe(viewLifecycleOwner) { response ->
            response?.let {
                viewModel.login2faNew(Login2faBodyNew(code = "1234", key = response.key))
            }
        }

        viewModel.successLogin2fa.observe(viewLifecycleOwner) { response ->
            response?.let {
                Constants.USER_TOKEN = response.accessToken
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()
            }
        }

        viewModel.error401.observe(viewLifecycleOwner) { error401 ->
            error401?.let {
                if (firstTimeMovingToDoneFragment) {
                    firstTimeMovingToDoneFragment = false
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterDoneFragment())
                }
            }
        }


        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {

                mostrarToast(it, requireContext())
                Log.i(TAG, "No fragment login error é : $error")
            }
        }
    }

    private fun setupUI() {
        preferences = Preferences(requireContext())

        val text = getString(R.string.ainda_n_o_tem_cadastro_clique_aqui)
        val spannableString = SpannableString(text)

        spannableString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)),
            26,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.labelCreate.text = spannableString
    }

    private fun onClick() {

        binding.btnSelectBrazil.setOnClickListener {
            setLanguagePt()
        }

        binding.btnSelectEUA.setOnClickListener {
            setLanguageEn()
        }

        binding.apply {
            buttonEnter.setOnClickListener {
                if (validateOfFields()) {
                    viewModel.login(
                        Login(
                            credential = email, password = password
                        )
                    )
                }
                firstTimeMovingToDoneFragment = true
                closeKeyboard()
            }

            labelForgot.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToForgotEmailFragment()
                )
            }

            labelCreate.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegisterProfileFragment()
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

    private fun setLanguagePt() {
        preferences.setLanguage("pt")
        setLocale("pt")
    }

    private fun setLanguageEn() {
        preferences.setLanguage("en")
        setLocale("en")
    }

    fun setLocale(lang: String?) {
        val myLocale = Locale(lang)
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        // Recriar o LoginFragment usando o NavController
        val navController = findNavController()
        navController.navigate(R.id.loginFragment)
    }

    private fun closeKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}