package com.draccoapp.poker.ui.fragments.contract.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.draccoapp.poker.api.pagging.adaptersPaginacao.AdapterPaginacaoOngoing
import com.draccoapp.poker.data.Contract
import com.draccoapp.poker.databinding.FragmentCurrentContractBinding
import com.draccoapp.poker.ui.adapters.ContractListAdapter
import com.draccoapp.poker.viewModel.ContractViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.HttpException


class CurrentContractFragment : Fragment() {


    private var _binding: FragmentCurrentContractBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ContractViewModel by viewModel()
    private lateinit var adapterPaginacaoOngoing: AdapterPaginacaoOngoing

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentContractBinding.inflate(inflater, container, false)
        adapterPaginacaoOngoing = AdapterPaginacaoOngoing()
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
                adapterPaginacaoOngoing.submitData(lifecycle, it)
                Log.i("ListaContracts", "Depois de chamar submitData")
            } catch (e: HttpException) {
                Log.e("ListaContracts", "Erro HTTP: ${e.code()} - ${e.message()}")
            } catch (e: Exception) {
                Log.e("ListaContracts", "Erro inesperado: ${e.message}")
            }
        }
    }


    private fun setupRecycler() {

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterPaginacaoOngoing
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}