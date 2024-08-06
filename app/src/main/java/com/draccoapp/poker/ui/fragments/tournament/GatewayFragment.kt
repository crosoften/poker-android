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
import com.draccoapp.poker.api.model.response.tournamentForms.Question
import com.draccoapp.poker.databinding.FragmentGatewayBinding
import com.draccoapp.poker.utils.converterDataNextTournament
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.TournamentViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel


class GatewayFragment : Fragment() {
    private val TAG = "GatewayFragment"

    private var _binding: FragmentGatewayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()
    var shortAnswerCounter = 1
    var dropdownCounter = 1
    var selectionCounter = 1

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
                        configRespostaHorizontalLayoutWithCircles(question, linearLayout)
//                        configRespostaHorizontalLayoutWithRadioButtons(question, linearLayout)
//                        configRespostaHorizontalLayout(question, linearLayout)
                    }

                    "dropdown" -> {
                        configPergunta(question, linearLayout)
                        configRespostaDropdown(question, linearLayout)
                    }

                    else -> {
                        mostrarToast("Tipo de pergunta desconhecido", requireContext())
                    }
                }
            }
        }//observer


    }

    val idsCheckBox = mutableListOf<String>()

    private fun configRespostaHorizontalLayoutWithCircles(question: Question, parentLinearLayout: LinearLayout) {
        question.options.forEach { option ->
            // Cria um LinearLayout horizontal
            val horizontalLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(16, 16, 16, 16)
                    gravity = Gravity.CENTER_VERTICAL // Define o alinhamento vertical para o conteúdo
                }
            }

            // Cria um TextView e define suas propriedades
            val textView = TextView(requireContext()).apply {
                text = option.option
                setTextColor(resources.getColor(R.color.white))
                textSize = 13f
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                    setPadding(16, 0, 16, 0)
                }
            }

            // Cria o círculo com borda
            val outerCircle = View(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    20.dpToPx(requireContext()), // Método de extensão para converter dp para px
                    20.dpToPx(requireContext())
                )

                background = ContextCompat.getDrawable(context, R.drawable.circle_outline_white)
            }

            // Cria o círculo cheio
            val innerCircle = View(requireContext()).apply {
                layoutParams = FrameLayout.LayoutParams(
                    10.dpToPx(requireContext()), // Método de extensão para converter dp para px
                    10.dpToPx(requireContext())
                ).apply {
                    gravity = Gravity.CENTER
                }

                background = ContextCompat.getDrawable(context, R.drawable.circle_filled)
                visibility = View.GONE // Inicialmente invisível
            }

            // Cria um FrameLayout para empilhar os círculos
            val frameLayout = FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(6, 0, 16, 0) }

                addView(outerCircle)
                addView(innerCircle)
            }

            // Adiciona o TextView e o FrameLayout com os círculos ao LinearLayout horizontal
            horizontalLayout.addView(frameLayout)
            horizontalLayout.addView(textView)

            // Adiciona o LinearLayout horizontal ao parentLinearLayout
            parentLinearLayout.addView(horizontalLayout)

            // Log para confirmar a adição dos círculos
            Log.i("Circles", "configRespostaHorizontalLayout: Círculos adicionados com sucesso")

            // Listener personalizado para alternar a visibilidade do círculo cheio
            frameLayout.setOnClickListener {
                innerCircle.visibility = if (innerCircle.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                outerCircle.background = if (innerCircle.visibility == View.VISIBLE) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_outline_red)
                } else ContextCompat.getDrawable(requireContext(), R.drawable.circle_outline_white)
                Log.i("Circles", "configRespostaHorizontalLayout: InnerCircle visibility é ${innerCircle.visibility}")
            }
        }
    }

//    private fun config

    private fun configRespostaDropdown(question: Question, linearLayout: LinearLayout) {
        val textInputLayout = TextInputLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 16, 16)
                isHintEnabled = false
                // Altera o ícone da seta para baixo
                endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
                setEndIconDrawable(R.drawable.ic_arrow_down)
                setEndIconTintList(ColorStateList.valueOf(resources.getColor(R.color.white))) // Altera a cor do ícone para branco
            }
        }

        // Cria um AutoCompleteTextView e define a configuração
        val styledContext = ContextThemeWrapper(requireContext(), R.style.TextInputLayoutDrop)
        val autoCompleteTextView = AutoCompleteTextView(styledContext).apply {
            hint = "Selecione uma opção"
            setHintTextColor(resources.getColor(R.color.white))
            setTextColor(resources.getColor(R.color.white))
            setBackgroundColor(resources.getColor(R.color.black))
            setBackgroundResource(R.drawable.bg_button_borda_vermelha)
            textSize = 13f
            setPadding(50, 0, 0, 0)
            inputType = InputType.TYPE_CLASS_TEXT
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50.dpToPx(requireContext()) // Método de extensão para converter dp para px
            )
            id = View.generateViewId() // Gera um ID único
            tag = "dropdown $dropdownCounter" // Define a tag incremental
        }
        // Incrementa o contador
        dropdownCounter++

        Log.i("GatewayFrag", "setupObserver: o id desse componente é ${autoCompleteTextView.id} \n e a tag é ${autoCompleteTextView.tag}")

        // Cria um adaptador com as opções
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, question.options.map { it.option })
        autoCompleteTextView.setAdapter(adapter)

        // Adiciona o AutoCompleteTextView ao TextInputLayout
        textInputLayout.addView(autoCompleteTextView)

        // Adiciona o TextInputLayout ao linearLayout
        linearLayout.addView(textInputLayout)
    }


    private fun configRespostaMultipleChoice(question: Question, linearLayout: LinearLayout) {
        val radioGroup = RadioGroup(requireContext())

        question.options.forEach { option ->
            val radioButton = RadioButton(requireContext()).apply {
                text = option.option
                setTextColor(resources.getColor(R.color.white))
                textSize = 13f

                // Define as margens para o RadioButton
                val layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setPadding(17,0,0,0)
                }
                this.layoutParams = layoutParams
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
            hint = "Digite sua LongAnswer aqui"
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
            textSize = 15f
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