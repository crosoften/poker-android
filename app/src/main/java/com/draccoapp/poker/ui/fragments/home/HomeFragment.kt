package com.draccoapp.poker.ui.fragments.home


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.data.Tournament
import com.draccoapp.poker.data.randomTournament
import com.draccoapp.poker.databinding.FragmentHomeBinding
import com.draccoapp.poker.databinding.FragmentSplashBinding
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var applicantAdapter: TournamentAdapter
    private lateinit var nextAdapter: TournamentAdapter
    private var listApplicant: MutableList<Tournament> = mutableListOf()
    private var listNext: MutableList<Tournament> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

            btnApplicantSeeAll.setOnClickListener {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToApplicantTournamentFragment()
                    )
            }

            btnNextSeeAll.setOnClickListener {
                findNavController()
                    .navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToNextTournamentFragment()
                    )
            }

        }
    }

    private fun initModels() {

        val numTournamentToAdd = 10

        lifecycleScope.launch {
            val tournamentToAdd = (1..numTournamentToAdd).map { randomTournament() }

            launch(Dispatchers.Main) {
                applicantAdapter.updateList(tournamentToAdd)
                nextAdapter.updateList(tournamentToAdd)
            }
        }

    }

    private fun setupRecycler() {

        applicantAdapter = TournamentAdapter(::onClickTournament)

        binding.recyclerDone.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = applicantAdapter
        }

        nextAdapter = TournamentAdapter(::onClickTournament)

        binding.recyclerNext.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = nextAdapter
        }
    }

    private fun onClickTournament(tournament: Tournament){
        findNavController()
            .navigate(
                HomeFragmentDirections
                    .actionHomeFragmentToDetailTournamentFragment(
                        tournament
                    )
            )
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}