package com.draccoapp.poker.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.draccoapp.poker.api.model.response.contract.ContractResponse
import com.draccoapp.poker.repository.GlobalRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ContractViewModel(private val repository: GlobalRepository) : ViewModel() {


    val list = repository.listarContractsPaginados().cachedIn(viewModelScope)

//    fun listarContracts(page: Int, context: Context) {
//        repository.listarContractsPaginados()
//
//    }


}