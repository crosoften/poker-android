package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.api.pagging.adaptersPaginacao.AdapterPaginacao
import com.draccoapp.poker.databinding.FragmentAllContractBinding
import com.draccoapp.poker.ui.adapters.Contract2Adapter
import com.draccoapp.poker.utils.mostrarToast
import com.draccoapp.poker.viewModel.ContractViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllContractFragment : Fragment() {
    private var _binding: FragmentAllContractBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContractViewModel by viewModel()
    private lateinit var adapterPaginacao: AdapterPaginacao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllContractBinding.inflate(inflater, container, false)
        adapterPaginacao = AdapterPaginacao()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.allContracts.observe(viewLifecycleOwner) {
            setupRecycler(it.data)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            mostrarToast(it, requireContext())
        }
    }

    private fun setupRecycler(contractList: List<Contract>) {
        binding.recycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = Contract2Adapter(
                contractList,
            ) {}
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}