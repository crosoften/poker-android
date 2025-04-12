package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.draccoapp.poker.api.model.response.contract.Contract
import com.draccoapp.poker.api.model.response.contract.ContractResponse
import com.draccoapp.poker.api.model.response.contract.details.ContractDetails
import com.draccoapp.poker.api.model.type.ContractStatus
import com.draccoapp.poker.repository.ContractRepository
import kotlinx.coroutines.launch

class ContractViewModel(private val repository: ContractRepository) : ViewModel() {
    val allContracts = MutableLiveData<ContractResponse>()
    val contractDetails = MutableLiveData<ContractDetails>()
    val error = MutableLiveData<String>()

    init {
        loadAllContracts()
    }

    fun loadAllContracts() {
        try {
            viewModelScope.launch {
                val result = repository.getAllContracts()
                allContracts.postValue(result)
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }

    fun loadContractDetails(contractId: String) {
        try {
            viewModelScope.launch {
                val result = repository.getContractById(contractId)
                contractDetails.postValue(result)
            }
        } catch (e: Exception) {
            error.postValue(e.message)
        }
    }

    fun filterContracts(status: String): List<Contract>? {
        val r = allContracts.value?.data?.filter {
            it.status == status
        }
        Log.i("contractTest", "filterContracts: ${allContracts.value}")
        return r
    }
}