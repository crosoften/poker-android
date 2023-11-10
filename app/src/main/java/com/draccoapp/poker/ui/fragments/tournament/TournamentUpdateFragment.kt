package com.draccoapp.poker.ui.fragments.tournament

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.R
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.data.randomTournament
import com.draccoapp.poker.databinding.FragmentAboutBinding
import com.draccoapp.poker.databinding.FragmentTournamentUpdateBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.adapters.UpdateTournamentAdapter
import com.draccoapp.poker.ui.fragments.profile.ProfileFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TournamentUpdateFragment : Fragment() {

    private var _binding: FragmentTournamentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var updateAdapter: UpdateTournamentAdapter

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

        onclick()
        setupRecycler()
        initModels()

    }

    private fun onclick() {

        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }



        }
    }

    private fun initModels() {

        val numTournamentToAdd = 10

        lifecycleScope.launch {
            val tournamentToAdd = (1..numTournamentToAdd).map { randomTournament() }

            launch(Dispatchers.Main) {
                updateAdapter.updateList(tournamentToAdd)
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

    private fun onClickTournament(tournament: Tournament){
        findNavController()
            .navigate(
                TournamentUpdateFragmentDirections
                    .actionTournamentUpdateFragmentToDetailUpdateFragment()
            )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}