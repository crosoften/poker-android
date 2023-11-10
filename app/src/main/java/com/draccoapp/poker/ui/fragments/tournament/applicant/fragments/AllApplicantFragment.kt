package com.draccoapp.poker.ui.fragments.tournament.applicant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.R
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.data.randomTournament
import com.draccoapp.poker.databinding.FragmentAllApplicantBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.adapters.TournamentListAdapter
import com.draccoapp.poker.ui.fragments.home.HomeFragmentDirections
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AllApplicantFragment : Fragment() {

    private var _binding: FragmentAllApplicantBinding? = null
    private val binding get() = _binding!!


    private lateinit var applicantAdapter: TournamentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllApplicantBinding.inflate(inflater, container, false)
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


        }
    }

    private fun initModels() {

        val numTournamentToAdd = 10

        lifecycleScope.launch {
            val tournamentToAdd = (1..numTournamentToAdd).map { randomTournament() }

            launch(Dispatchers.Main) {
                applicantAdapter.updateList(tournamentToAdd)
            }
        }

    }

    private fun setupRecycler() {

        applicantAdapter = TournamentListAdapter(::onClickTournament)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = applicantAdapter
        }
    }

    private fun onClickTournament(tournament: Tournament){
//        findNavController()
//            .navigate(
//                HomeFragmentDirections
//                    .actionHomeFragmentToDetailTournamentFragment(
//                        tournament
//                    )
//            )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}