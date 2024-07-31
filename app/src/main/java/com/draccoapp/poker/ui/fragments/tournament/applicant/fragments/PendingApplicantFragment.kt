package com.draccoapp.poker.ui.fragments.tournament.applicant.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.FragmentPendingApplicantBinding
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.ui.adapters.TournamentListAdapter
import com.draccoapp.poker.ui.fragments.tournament.applicant.ApplicantTournamentFragmentDirections
import com.draccoapp.poker.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class PendingApplicantFragment : Fragment() {

    private var _binding: FragmentPendingApplicantBinding? = null
    private val binding get() = _binding!!

    private val TAG = "PendingApplicantFragment"
    private val viewModel : UserViewModel by viewModel()

    private lateinit var applicantAdapter: TournamentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPendingApplicantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
        onclick()
        setupRecycler()
    }

    private fun onclick() {

    }

    private fun setupObserver() {

//        viewModel.getTournamentsJoinedByUser("pending")

        viewModel.tournamentApplicant.observe(viewLifecycleOwner) { response ->
            applicantAdapter.updateList(response)
            applicantAdapter.setUnit(viewModel.getUnit())
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.root.showSnackBarRed(it)
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
//                ApplicantTournamentFragmentDirections
//                    .actionApplicantTournamentFragmentToDetailTournamentFragment(
//                        tournament
//                    )
//            )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}