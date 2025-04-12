package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.draccoapp.poker.api.model.response.DetailsUpdateTournament
import com.draccoapp.poker.databinding.FragmentDetailUpdateBinding
import com.draccoapp.poker.viewModel.TournamentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailUpdateFragment : Fragment() {

    private var _binding: FragmentDetailUpdateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TournamentViewModel by viewModel()
    private val args by navArgs<DetailUpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUpdateDetails(args.idUpdate)
        onclick()
        setupObserver()

    }

    private fun setupObserver() {
        viewModel.successGetDetailsUpdadte.observe(viewLifecycleOwner) {
            setupView(it)
        }
    }

    private fun setupView(it: DetailsUpdateTournament) {
        with(binding){
            imageView2.load(it.proofUrl)
            textView13.text= it.title
            textView18.text= it.message
        }
    }

    private fun onclick() {

        binding.apply {

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