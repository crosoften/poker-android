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
//    var shortAnswerCounter = 1
//    var dropdownCounter = 1

    private val editTextList = mutableListOf<TextInputEditText>()
    private val editTextLongList = mutableListOf<TextInputEditText>()
    private val selectedRadioButtonsMap = mutableMapOf<Int, RadioButton>()

    private val dropDownList = mutableListOf<AutoCompleteTextView>()
    private val selectionBoxTexts = mutableListOf<String>()
    private var questionIdCounter = 0



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
        Glide.with(requireContext()).load(tournament.imageUrl).into(binding.imageView2)

        tournament.title.let { binding.textView13.text = it }
        tournament.startDatetime.let { binding.textView14.text = converterDataNextTournament(it.toString()) }
        tournament.prize.let { binding.textView16.text = it.toString() }
    }

    private fun setupObserver() {
        viewModel.successGetTournamentForms.observe(viewLifecycleOwner) {
            val questions = it.form.questions
            Log.i(TAG, "setupObserver: As questions são $questions")

            val linearLayout = binding.linLayQuestions
            linearLayout.removeAllViews()

            // Adiciona dinamicamente TextViews para cada pergunta
            questions.forEach { question ->

                val questionId = generateQuestionId()

                // Atualize a pergunta com o ID gerado
                val questionWithId = question.copy(id = questionId)

                when (questionWithId .type) {
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
                        configRespostaMultipleChoice(question, linearLayout, questionId)
                    }

                    "selectionBox" -> {
                        configPergunta(question, linearLayout)
                        configRespostaHorizontalLayoutWithCircles(question, linearLayout)
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

        viewModel.successSubscribeTournament.observe(viewLifecycleOwner) {
            findNavController().navigate(
                GatewayFragmentDirections.actionGatewayFragmentToSubscribeTournamentFragment()
            )
           // findNavController().popBackStack(R.id.homeFragment, false)
           // mostrarToast("Torneio inscrito com sucesso", requireContext())
        }
    }


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

            // Listener personalizado para alternar a visibilidade do círculo cheio
            frameLayout.setOnClickListener {
                innerCircle.visibility = if (innerCircle.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                outerCircle.background = if (innerCircle.visibility == View.VISIBLE) {
                    ContextCompat.getDrawable(requireContext(), R.drawable.circle_outline_red)
                } else ContextCompat.getDrawable(requireContext(), R.drawable.circle_outline_white)


                //se o iner tiver visible vc adiciona e se o invisível vc remove
                if (innerCircle.visibility == View.VISIBLE) {
                    selectionBoxTexts.add(textView.text.toString())

                } else {
                    selectionBoxTexts.remove(textView.text.toString())
                }

                Log.i("Circles", "configRespostaHorizontalLayout: InnerCircle visibility é ${innerCircle.visibility}")
            }
        }
    }

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
            inputType = InputType.TYPE_NULL
            isFocusable = false
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                50.dpToPx(requireContext()) // Método de extensão para converter dp para px
            )
//            id = View.generateViewId() // Gera um ID único
            dropDownList.add(this)
        }
        // Cria um adaptador com as opções
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, question.options.map { it.option })
        autoCompleteTextView.setAdapter(adapter)

        textInputLayout.addView(autoCompleteTextView)
        linearLayout.addView(textInputLayout)
    }

    private fun configRespostaMultipleChoice(question: Question, linearLayout: LinearLayout, questionId: Int) {
//        val radioGroup = RadioGroup(requireContext())
        val radioGroup = RadioGroup(requireContext()).apply {
            id = questionId  // Adicione um ID único para o RadioGroup

            // Adicione um listener para monitorar as mudanças na seleção
            setOnCheckedChangeListener { group, checkedId ->
                // Limpe a seleção anterior da questão atual
                selectedRadioButtonsMap.remove(group.id)

                // Encontre o RadioButton selecionado e adicione-o à lista
                val selectedRadioButton = findViewById<RadioButton>(checkedId)
                selectedRadioButton?.let {
                    selectedRadioButtonsMap[group.id] = it
                }
            }
        }

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
                    setPadding(17, 0, 0, 0)
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

//            tag = "shortAnswer $shortAnswerCounter" // Define a tag incremental
            editTextList.add(this)
        }
        // Adiciona o TextInputEditText ao TextInputLayout
        textInputLayout.addView(textInputEditText)
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
            setPadding(50, 44, 0, 44)
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            maxLines = Int.MAX_VALUE
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT// Método de extensão para converter dp para px
            )
            id = View.generateViewId() // Gera um ID único
            editTextLongList.add(this)

        }
        // Adiciona o TextInputEditText ao TextInputLayout
        textInputLayout.addView(textInputEditText)
        linearLayout.addView(textInputLayout)
    }

    private fun configPergunta(question: Question, linearLayout: LinearLayout) {
        val textView = TextView(context).apply {
            Log.i("QuestionReq", "configPergunta: question.required é : ${question.required}")
            if (question.required) {
                text = question.question.trim() + " *"
            } else text = question.question.trim()

            textSize = 15f
            setTextColor(resources.getColor(R.color.white))
            setPadding(16, 16, 16, 16)
        }
        linearLayout.addView(textView)
    }

    private fun onClick() {

        binding.apply {

            buttonInscrever.setOnClickListener {
                val answersList = mutableListOf<String>()


                editTextList.forEach { editText ->
                    val answer = editText.text.toString()
                    Log.i("Respostas", "O texto da resposta curta é $answer")
                    answersList.add(answer)
                }

                editTextLongList.forEach { editText ->
                    val answer = editText.text.toString()
                    Log.i("Respostas", "O texto da resposta longas é $answer")
                    answersList.add(answer)
                }

                selectedRadioButtonsMap.values.forEach { radioButton ->
                    val answer = radioButton.text.toString()
                    Log.i("Respostas", "onClick: O texto do radio Button selecionado  é $answer")
                    answersList.add(answer)
                }
//                Log.i("Respostas", "onClick: a lista de RADIO é $selectedRadioButtons")


                dropDownList.forEach { dropdown ->
                    val answer = dropdown.text.toString()
                    Log.i("Respostas", "onClick: O texto do dropdown é $answer")
                    answersList.add(answer)
                }

                Log.i("Respostas", "onClick: O texto do selectionBox é $selectionBoxTexts")
                val joinedString = selectionBoxTexts.joinToString(separator = ", ")

                answersList.add(joinedString)

                val bodyAnswers = AnswerBody(answersList)
                bodyAnswers.answer.forEach {
                    Log.i("Respostas", "um único item foi ::  $it")
                }
                tournament.id?.let {
                    viewModel.subscribeToTournament(idTounament = it, answerBody = bodyAnswers)
                }
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

    // Função para gerar IDs únicos
    private fun generateQuestionId(): Int {
        return questionIdCounter++
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}