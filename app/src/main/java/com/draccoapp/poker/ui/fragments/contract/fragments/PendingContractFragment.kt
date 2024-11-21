package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.api.model.type.ContractStatus
import com.draccoapp.poker.api.pagging.adaptersPaginacao.AdapterPaginacaoPending
import com.draccoapp.poker.databinding.FragmentPendingContractBinding
import com.draccoapp.poker.ui.adapters.Contract2Adapter
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.ContractViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException


class PendingContractFragment : Fragment() {

    private var _binding: FragmentPendingContractBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterPaginacaoPending: AdapterPaginacaoPending
    private val viewModel: ContractViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPendingContractBinding.inflate(inflater, container, false)
        adapterPaginacaoPending = AdapterPaginacaoPending()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        viewModel.allContracts.observe(viewLifecycleOwner){
            viewModel.filterContracts("pending")?.let {
                setupRecycler(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            mostrarToast(it, requireContext())
        }
    }

    private fun setupRecycler(pendingContracts: List<Contract>) {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = Contract2Adapter(pendingContracts){}
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }


}