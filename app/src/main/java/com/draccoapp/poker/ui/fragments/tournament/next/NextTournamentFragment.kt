package com.draccoapp.poker.ui.fragments.tournament.next

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.model.response.homeFrament.NextTournament
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.databinding.FragmentNextTournamentBinding
import com.draccoapp.poker.extensions.showSnackBarRed
import com.draccoapp.poker.ui.adapters.TournamentListAdapter
import com.draccoapp.poker.ui.fragments.home.HomeFragmentDirections
import com.draccoapp.poker.viewModel.HomeViewModel
import com.draccoapp.poker.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class NextTournamentFragment : Fragment() {

    private var _binding: FragmentNextTournamentBinding? = null
    private val binding get() = _binding!!
    private val TAG = "NextTournamentFragment"
    private val viewModel: UserViewModel by viewModel()
    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var nextAdapter: TournamentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNextTournamentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getHomeFragment()
        setupObserver()
        onclick()
        setupRecycler()
    }

    private fun onclick() {
        binding.apply {

            back.setOnClickListener {
                findNavController()
                    .popBackStack()
            }
        }
    }

    private fun setupObserver() {

//        viewModel.getTournamentsAvailableToUser()
        homeViewModel.successHomeFragment.observe(viewLifecycleOwner) { response ->
            response.nextTournaments?.let { nextAdapter.updateList(it) }
        }

//        viewModel.tournamentsByUser.observe(viewLifecycleOwner) { response ->
//            nextAdapter.updateList(response)
//            nextAdapter.setUnit(viewModel.getUnit())
//        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.root.showSnackBarRed(it)
            }
        }
    }

    private fun setupRecycler() {

        nextAdapter = TournamentListAdapter(::onClickTournament)

        binding.recycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = nextAdapter
        }
    }

    private fun onClickTournament(tournament: NextTournament) {

        findNavController().navigate(
            NextTournamentFragmentDirections.actionNextTournamentFragmentToDetailTournamentFragment(
                tournament,
                null,
                "null",
                "next"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}