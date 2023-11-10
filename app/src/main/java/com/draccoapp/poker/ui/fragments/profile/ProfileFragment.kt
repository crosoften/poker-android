package com.draccoapp.poker.ui.fragments.profile

import android.content.Intent
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
import com.draccoapp.poker.databinding.FragmentProfileBinding
import com.draccoapp.poker.databinding.FragmentSplashBinding
import com.draccoapp.poker.ui.activities.AccountActivity
import com.draccoapp.poker.ui.adapters.TournamentAdapter
import com.draccoapp.poker.ui.fragments.home.HomeFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var applicantAdapter: TournamentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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
                        ProfileFragmentDirections
                            .actionProfileFragmentToApplicantTournamentFragment()
                    )
            }

            buttonProfileAbout.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToAboutFragment()
                    )
            }

            buttonProfileContract.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToContractFragment()
                    )
            }

            buttonProfileHelp.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToCenterHelpFragment()
                    )
            }

            buttonProfileTerms.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToTermsUseFragment()
                    )
            }

            buttonProfilePolitic.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToPoliticPrivacyFragment()
                    )
            }

            buttonProfileContact.setOnClickListener {
                findNavController()
                    .navigate(
                        ProfileFragmentDirections
                            .actionProfileFragmentToContactFragment()
                    )
            }

            buttonProfileLogout.setOnClickListener {
                startActivity(Intent(requireContext(), AccountActivity::class.java))
                requireActivity().finishAffinity()
            }

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

        applicantAdapter = TournamentAdapter(::onClickTournament)

        binding.recyclerDone.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = applicantAdapter
        }


    }

    private fun onClickTournament(tournament: Tournament){
        findNavController()
            .navigate(
                ProfileFragmentDirections
                    .actionProfileFragmentToDetailTournamentFragment(
                        tournament
                    )
            )
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}