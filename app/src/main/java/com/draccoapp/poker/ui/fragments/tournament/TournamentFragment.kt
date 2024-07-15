package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.databinding.FragmentTournamentBinding
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt


class TournamentFragment : Fragment() {

    private var _binding: FragmentTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()
    private lateinit var preferences: Preferences
    private lateinit var listaDeEstadosBrasileiros: MutableList<String>
    private lateinit var listaDeEstadosEUA: MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTournamentBinding.inflate(inflater, container, false)
        preferences = Preferences(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inicializaListaDeEstados()
        configMoedaAtual()
        configEditState()
        configEditCountry()
        setup()

    }

    private fun configEditCountry() {
        val listaDePaises = mutableListOf(
            "Brasil",
            "Estados Unidos"
        )

        val editCountry = binding.editCountry
        editCountry.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaDePaises))

        binding.editCountry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val country = s.toString()
                val listaCorreta = if (country == "Brasil") {
                    listaDeEstadosBrasileiros
                } else {
                    listaDeEstadosEUA
                }
                atualizarListaEstados(listaCorreta)
            }
        })
    }

    fun atualizarListaEstados(lista: List<String>) {
        val editState = binding.editState
        editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, lista))
    }


    private fun configEditState() {
        val listaCorreta = if (binding.editCountry.text.toString() == "Brasil") {
            listaDeEstadosBrasileiros
        } else {
            listaDeEstadosEUA
        }


        val editState = binding.editState
        editState.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, listaCorreta))
    }


    private fun configMoedaAtual() {
        val language = preferences.getLanguage()
        val moedaAtual = if (language == "pt") "(R$)" else "($)"
        binding.txtMoedaAtual.text = moedaAtual
    }


    private fun setup() {
        binding.buttonConfirmar.setOnClickListener {
            val editName = binding.editName.text.toString()
            val editAward = binding.editAward.text.toString()
            var editAwardInt: Int? = 0
            editAward?.let {
                editAwardInt = arredondarValor(editAward)
            }
            val editCountry = binding.editCountry.text.toString()
            val editState = binding.editState.text.toString()
            val editCity = binding.editCity.text.toString()
            val editLink = binding.editLink.text.toString()

            val body = TournamentBodyNew(
                title = editName,
                prize = editAwardInt!!,
                country = editCountry,
                state = editState,
                city = editCity,
                eventUrl = editLink,
                proofUrl = ""
            )

            Log.i("torneio", "setup: o body do torneio é $body")


            findNavController().popBackStack(R.id.homeFragment, false)
        }


    }

    fun arredondarValor(valor: String): Int? {
        return valor.toDoubleOrNull()?.roundToInt()
    }

    private fun inicializaListaDeEstados() {
        listaDeEstadosBrasileiros = mutableListOf(
            "Acre",
            "Alagoas",
            "Amapá",
            "Amazonas",
            "Bahia",
            "Ceará",
            "Distrito Federal",
            "Espírito Santo",
            "Goiás",
            "Maranhão",
            "Mato Grosso",
            "Mato Grosso do Sul",
            "Minas Gerais",
            "Pará",
            "Paraíba",
            "Paraná",
            "Pernambuco",
            "Piauí",
            "Rio de Janeiro",
            "Rio Grande do Norte",
            "Rio Grande do Sul",
            "Rondônia",
            "Roraima",
            "Santa Catarina",
            "São Paulo",
            "Sergipe",
            "Tocantins"
        )

        listaDeEstadosEUA = mutableListOf(
            "Alabama",
            "Alaska",
            "Arizona",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "Florida",
            "Georgia",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada",
            "New Hampshire",
            "New Jersey",
            "New Mexico",
            "New York",
            "North Carolina",
            "North Dakota",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania",
            "Rhode Island",
            "South Carolina",
            "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia",
            "Washington",
            "West Virginia",
            "Wisconsin",
            "Wyoming"
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()


        _binding = null
    }

}