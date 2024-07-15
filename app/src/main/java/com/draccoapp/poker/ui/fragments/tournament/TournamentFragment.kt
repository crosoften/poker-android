package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentSplashBinding
import com.draccoapp.poker.databinding.FragmentTournamentBinding
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.SharedUtils
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TournamentFragment : Fragment() {

    private var _binding: FragmentTournamentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TournamentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTournamentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()

        val preferences = Preferences(requireContext())
//        val language = SharedUtils.getValueInSharedPreferences("LANGUAGE_KEY")
        val language = preferences.getLanguage()
        mostrarToast("A linguagem atual é $language", requireContext())

    }


    private fun setup() {

        val editAward = binding.editAward

        editAward.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return

                isEditing = true

                if (s.toString().isEmpty()) {
                    editAward.setText("R$ ")
                    editAward.setSelection(editAward.text!!.length)
                } else if (!s.toString().startsWith("R$ ")) {
                    editAward.setText("R$ " + s.toString().replace("R$", "").trim())
                    editAward.setSelection(editAward.text!!.length)
                }

                isEditing = false
            }
        })




        binding.buttonConfirmar.setOnClickListener {
            findNavController().popBackStack(R.id.homeFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}