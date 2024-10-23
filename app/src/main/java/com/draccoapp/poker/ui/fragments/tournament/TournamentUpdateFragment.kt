package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.response.updateTournament.UpdateTournamentData
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.data.randomTournament
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentTournamentUpdateBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.adapters.UpdateTournamentAdapter
import com.draccoapp.poker.ui.fragments.profile.ProfileFragmentDirections
import com.draccoapp.poker.viewModel.TournamentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class TournamentUpdateFragment : Fragment() {

    private var _binding: FragmentTournamentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var updateAdapter: UpdateTournamentAdapter

    private val viewModel: TournamentViewModel by viewModel()

    private val args: TournamentUpdateFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentTournamentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavorites(args.subscriptionId)
        onclick()
        setupRecycler()
        setupObserver()

        if (args.status != "approved"){
            binding.imageView4.visibility = View.GONE
        }else{
            binding.imageView4.visibility = View.VISIBLE
        }


    }

    private fun setupObserver() {
        viewModel.successUpdateTournament.observe(viewLifecycleOwner) {
            it.data?.let { it1 -> updateAdapter.updateList(it1) }
        }
    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }

            imageView4.setOnClickListener {
                findNavController()
                    .navigate(
                        TournamentUpdateFragmentDirections
                            .actionTournamentUpdateFragmentToAddUpdateFragment(args.subscriptionId)
                    )
            }



        }
    }



    private fun setupRecycler() {

        updateAdapter = UpdateTournamentAdapter(::onClickTournament)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = updateAdapter
        }



    }

    private fun onClickTournament(tournament: UpdateTournamentData){
        findNavController()
            .navigate(
                TournamentUpdateFragmentDirections
                    .actionTournamentUpdateFragmentToDetailUpdateFragment(tournament.id!!)
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}