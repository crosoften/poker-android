package com.draccoapp.poker.ui.fragments.tournament

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.tournamentForms.Question
import com.draccoapp.poker.databinding.FragmentGatewayBinding
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class GatewayFragment : Fragment() {

    private var _binding: FragmentGatewayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()
    var shortAnswerCounter = 1


    private val args by navArgs<GatewayFragmentArgs>()
    private val tournament by lazy {
        args.tournament
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGatewayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tournament.id?.let {
            viewModel.getTournament(it)
        }
        setupObserver()
        setupUI()
        onClick()
    }



    private fun setupUI() {

        Glide.with(requireContext())
            .load(tournament.imageUrl)
            .into(binding.imageView2)

        tournament.title.let {
            binding.textView13.text = it
        }

        tournament.startDatetime.let {
            binding.textView14.text = converterDataNextTournament(it.toString())
        }

        tournament.prize.let {
            binding.textView16.text = it.toString()
        }
    }

    private fun setupObserver() {
        viewModel.successGetTournamentForms.observe(viewLifecycleOwner) {
            val questions = it.form.questions
            Log.i(TAG, "setupObserver: As questions são $questions")

            val linearLayout = binding.linLayQuestions
            linearLayout.removeAllViews()

            // Adiciona dinamicamente TextViews para cada pergunta
            questions.forEach { question ->

                when (question.type) {
                    "shortAnswer" -> {
                        configPergunta(question, linearLayout)
                        configRespostaShortAnswer(linearLayout)
                    }

                    "longAnswer" -> {
                        configPergunta(question, linearLayout)
                        configRespostaLongAnswer(linearLayout)
                    }

                    "multipleChoice" -> {
                        configPergunta(question, linearLayout)
                        configRespostaMultipleChoice(question, linearLayout)



                    }

                    "selectionBox" -> {
                        configPergunta(question, linearLayout)
                    }

                    "dropdown" -> {
                        configPergunta(question, linearLayout)
                    }

                    else -> {
                        mostrarToast("Tipo de pergunta desconhecido", requireContext())
                    }
                }
            }
        }//observer


    }

    private fun configRespostaMultipleChoice(question: Question, linearLayout: LinearLayout) {
        val radioGroup = RadioGroup(requireContext())

        question.options.forEach { option ->
            val radioButton = RadioButton(requireContext()).apply {
                text = option.option
                setTextColor(resources.getColor(R.color.white))
            }
            radioGroup.addView(radioButton)
        }

        linearLayout.addView(radioGroup)
    }

    private fun configRespostaShortAnswer(linearLayout: LinearLayout) {
        val textInputLayout = TextInputLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 16, 16)
                isHintEnabled = false
            }
        }

        // Cria um TextInputEditText e define a configuração
        val textInputEditText = TextInputEditText(requireContext()).apply {
            hint = "Digite sua resposta aqui"
            setHintTextColor(resources.getColor(R.color.white))
            setTextColor(resources.getColor(R.color.white))
            setBackgroundColor(resources.getColor(R.color.black))
            setBackgroundResource(R.drawable.bg_button_borda_vermelha)
            textSize = 13f
            setPadding(50, 0, 0, 0)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            maxLines = 1
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50.dpToPx(requireContext()) // Método de extensão para converter dp para px
            )
            id = View.generateViewId() // Gera um ID único
            tag = "shortAnswer $shortAnswerCounter" // Define a tag incremental
        }
        // Incrementa o contador
        shortAnswerCounter++

        Log.i("GatewayFrag", "setupObserver: o id desse componente é ${textInputEditText.id} \n e a tag é ${textInputEditText.tag}")

        // Adiciona o TextInputEditText ao TextInputLayout
        textInputLayout.addView(textInputEditText)

        // Adiciona o TextInputLayout ao linearLayout
        linearLayout.addView(textInputLayout)
    }

    private fun configRespostaLongAnswer(linearLayout: LinearLayout) {
        val textInputLayout = TextInputLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 16, 16)
                isHintEnabled = false
            }
        }

        // Cria um TextInputEditText e define a configuração
        val textInputEditText = TextInputEditText(requireContext()).apply {
            hint = "Digite sua resposta aqui"
            setHintTextColor(resources.getColor(R.color.white))
            setTextColor(resources.getColor(R.color.white))
            setBackgroundColor(resources.getColor(R.color.black))
            setBackgroundResource(R.drawable.bg_button_borda_vermelha)
            textSize = 13f
            setPadding(50, 0, 0, 0)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            maxLines = 6
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50.dpToPx(requireContext()) // Método de extensão para converter dp para px
            )
            id = View.generateViewId() // Gera um ID único
            tag = "shortAnswer $shortAnswerCounter" // Define a tag incremental
        }
        // Incrementa o contador
        shortAnswerCounter++

        Log.i("GatewayFrag", "setupObserver: o id desse componente é ${textInputEditText.id} \n e a tag é ${textInputEditText.tag}")

        // Adiciona o TextInputEditText ao TextInputLayout
        textInputLayout.addView(textInputEditText)

        // Adiciona o TextInputLayout ao linearLayout
        linearLayout.addView(textInputLayout)
    }


    private fun configPergunta(question: Question, linearLayout: LinearLayout) {
        val textView = TextView(context).apply {
            text = question.question.trim()
            textSize = 14f
            setTextColor(resources.getColor(R.color.white))
            setPadding(16, 16, 16, 16)
        }
        linearLayout.addView(textView)
    }

    private fun onClick() {

        binding.apply {

            buttonInscrever.setOnClickListener {
//                viewModel.entryTournament(
//                    Entry(
//                        requireContext().getPreferenceData().getUserId(),
//                        tournament.id,
//                        listOf("string")
//                    )
//                )

            }

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }
        }

    }

    // Método de extensão para converter dp para px
    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}