package com.draccoapp.poker.ui.fragments.tournament

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.AnswerBody
import com.draccoapp.poker.api.model.response.tournamentForms.Question
import com.draccoapp.poker.databinding.FragmentFinishTournamentBinding
import com.draccoapp.poker.databinding.FragmentGatewayBinding
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class FinishTournamentFragment : Fragment() {

    private var _binding: FragmentFinishTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()




    private val args by navArgs<FinishTournamentFragmentArgs>()
    private val tournament by lazy {
        args.idTournament
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFinishTournamentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tournament.let {
            viewModel.getTournament(it)
        }
        setupObserver()
//        setupUI()
        onClick()
    }


//    private fun setupUI() {
//        Glide.with(requireContext()).load(tournament.imageUrl).into(binding.imageView2)
//
//        tournament.title.let { binding.textView13.text = it }
//        tournament.startDatetime.let { binding.textView14.text = converterDataNextTournament(it.toString()) }
//        tournament.prize.let { binding.textView16.text = it.toString() }
//    }

    private fun setupObserver() {
        viewModel.successGetTournamentForms.observe(viewLifecycleOwner) {
            val questions = it.form.questions

        }//observer

        viewModel.successSubscribeTournament.observe(viewLifecycleOwner) {
            findNavController().navigate(
                GatewayFragmentDirections.actionGatewayFragmentToSubscribeTournamentFragment()
            )
           // findNavController().popBackStack(R.id.homeFragment, false)
           // mostrarToast("Torneio inscrito com sucesso", requireContext())
        }
    }



    private fun onClick() {

        binding.apply {

            buttonInscrever.setOnClickListener {
                val answersList = mutableListOf<String>()

//                tournament.id?.let {
//                    viewModel.subscribeToTournament(idTounament = it, answerBody = bodyAnswers)
//                }
            }

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}