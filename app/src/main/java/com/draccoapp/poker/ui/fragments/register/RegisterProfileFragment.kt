package com.draccoapp.poker.ui.fragments.register

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentRegisterProfileBinding
import com.draccoapp.poker.utils.MaskEditUtil


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
        val termsOfUse = getString(R.string.termos_de_uso)
        val politicOfPrivacy = getString(R.string.pol_tica_de_privacidade)
        val text = buildString {
            append(getString(R.string.declaro_que_concordo_e_aceito_os))
            append(termsOfUse)
            append(getString(R.string.e))
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


        mascaraDataNascimento()

        configEditGender()
        configEditState()



    }

    private fun configEditState() {
        val listaDeEstados = mutableListOf(
            "AC", // Acre
            "AL", // Alagoas
            "AP", // Amapá
            "AM", // Amazonas
            "BA", // Bahia
            "CE", // Ceará
            "DF", // Distrito Federal
            "ES", // Espírito Santo
            "GO", // Goiás
            "MA", // Maranhão
            "MT", // Mato Grosso
            "MS", // Mato Grosso do Sul
            "MG", // Minas Gerais
            "PA", // Pará
            "PB", // Paraíba
            "PR", // Paraná
            "PE", // Pernambuco
            "PI", // Piauí
            "RJ", // Rio de Janeiro
            "RN", // Rio Grande do Norte
            "RS", // Rio Grande do Sul
            "RO", // Rondônia
            "RR", // Roraima
            "SC", // Santa Catarina
            "SP", // São Paulo
            "SE", // Sergipe
            "TO"  // Tocantins
        )
        val editState = binding.editState
        editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeEstados))
    }

    private fun configEditGender() {
        val listaDeGeneros = mutableListOf("Male", "Female")
        val editGender = binding.editGender
        editGender.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDeGeneros))
    }

    private fun mascaraDataNascimento() {
        binding.editDate.addTextChangedListener(MaskEditUtil.mask(binding.editDate, MaskEditUtil.FORMAT_DATE))
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