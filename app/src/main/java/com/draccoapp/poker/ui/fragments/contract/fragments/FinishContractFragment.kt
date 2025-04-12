package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.api.model.type.ContractStatus
import com.draccoapp.poker.api.pagging.adaptersPaginacao.AdapterPaginacaoFinished
import com.draccoapp.poker.databinding.FragmentFinishContractBinding
import com.draccoapp.poker.ui.adapters.Contract2Adapter
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.ContractViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FinishContractFragment : Fragment() {

    private var _binding: FragmentFinishContractBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContractViewModel by viewModel()
    private lateinit var adapterPaginacaoFinished: AdapterPaginacaoFinished


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFinishContractBinding.inflate(inflater, container, false)
        adapterPaginacaoFinished = AdapterPaginacaoFinished()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.allContracts.observe(viewLifecycleOwner){
            viewModel.filterContracts("closed")?.let {
                setupRecycler(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
            mostrarToast(it, requireContext())
        }
    }

    private fun setupRecycler(contracts: List<Contract>) {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = Contract2Adapter(
                contracts,
            ) {}
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}