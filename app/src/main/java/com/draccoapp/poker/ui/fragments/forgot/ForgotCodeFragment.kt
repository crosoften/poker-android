package com.draccoapp.poker.ui.fragments.forgot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentForgotCodeBinding
import com.draccoapp.poker.ui.activities.MainActivity
import com.draccoapp.poker.utils.CodeValidatedHandler
import com.draccoapp.poker.extensions.showSnackBarRed


class ForgotCodeFragment : Fragment() {

    private var _binding: FragmentForgotCodeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentForgotCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()
        setupUI()

    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

        }
    }

    private fun setupUI() {
        val codeValidatedHandler = CodeValidatedHandler(binding.includeCodeValidated.root, binding.buttonEnter)

        val enter = findNavController().previousBackStackEntry?.destination?.label.toString()

        Log.e("ForgotCodeFragment", "setupUI: $enter")



        binding.buttonEnter.setOnClickListener {
            if (codeValidatedHandler.isAllDigitsEntered()) {
                when (enter) {
                    "ForgotEmailFragment" -> {
                        findNavController()
                            .navigate(
                                ForgotCodeFragmentDirections
                                    .actionForgotCodeFragmentToForgotPasswordFragment()
                            )
                    }
                    else -> {
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        requireActivity().finishAffinity()
                    }
                }

            } else {
                binding.root.showSnackBarRed(getString(R.string.por_favor_insira_todos_os_digitos))
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}