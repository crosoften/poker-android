package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.R
import com.draccoapp.poker.data.Contract
import com.draccoapp.poker.data.randomContract
import com.draccoapp.poker.databinding.FragmentAllContractBinding
import com.draccoapp.poker.databinding.FragmentCurrentContractBinding
import com.draccoapp.poker.ui.adapters.ContractListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CurrentContractFragment : Fragment() {


    private var _binding: FragmentCurrentContractBinding? = null
    private val binding get() = _binding!!


    private lateinit var contractListAdapter: ContractListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentContractBinding.inflate(inflater, container, false)
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

        val numContractToAdd = 10

        lifecycleScope.launch {
            val contractToAdd = (1..numContractToAdd).map { randomContract() }

            launch(Dispatchers.Main) {
                contractListAdapter.updateList(contractToAdd)
            }
        }

    }

    private fun setupRecycler() {

        contractListAdapter = ContractListAdapter(::onClickContract)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contractListAdapter
        }
    }

    private fun onClickContract(contract: Contract){
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