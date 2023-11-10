package com.draccoapp.poker.ui.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.draccoapp.poker.R
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentTermsUseBinding


class TermsUseFragment : Fragment() {

    private var _binding: FragmentTermsUseBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTermsUseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onclick()

    }

    private fun onclick() {

        binding.apply {


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}