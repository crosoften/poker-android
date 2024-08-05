package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.pagging.adaptersPaginacao.AdapterPaginacao
import com.draccoapp.poker.data.Contract
import com.draccoapp.poker.databinding.FragmentAllContractBinding
import com.draccoapp.poker.ui.adapters.ContractListAdapter
import com.draccoapp.poker.viewModel.ContractViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException


class AllContractFragment : Fragment() {


    private var _binding: FragmentAllContractBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContractViewModel by viewModel()
    private lateinit var adapterPaginacao: AdapterPaginacao


    private lateinit var contractListAdapter: ContractListAdapter

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

        setupRecycler()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.list.observe(viewLifecycleOwner) {
            Log.i("ListaContracts", "setupObserver: RECEBI OS CONSTRATOS $it")

            try {
                adapterPaginacao.submitData(lifecycle, it)
                Log.i("ListaContracts", "Depois de chamar submitData")
            } catch (e: HttpException) {
                Log.e("ListaContracts", "Erro HTTP: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.e("ListaContracts", "Erro inesperado: ${e.message}")
            }
        }
    }




    private fun setupRecycler() {

//        contractListAdapter = ContractListAdapter(::onClickContract)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterPaginacao
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}