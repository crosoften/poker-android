package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.api.model.response.TournamentResponseNew
import com.draccoapp.poker.api.modelOld.request.Entry
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.repository.TournamentRepository
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.launch

class TournamentViewModel(
    private val repository: TournamentRepository,
    private val preferences: Preferences
) : ViewModel() {

    val successCreateTournament = MutableLiveData<TournamentResponseNew>()


    val error: LiveData<String>
        get() = _error

    private val _error = MutableLiveData<String>()

    val appState: LiveData<DataState>
        get() = _appState

    private val _appState = MutableLiveData<DataState>()

    val entry: LiveData<Unit>
        get() = _entry

    private val _entry = MutableLiveData<Unit>()

    fun createTournament(body: TournamentBodyNew){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.createTournament(body)
            Log.e("TournamentViewModel", "O result da criação do torneio foi : $result")

            result.fold(
                onSuccess = {
                    _appState.value = DataState.Success
                    successCreateTournament.value = it
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Não foi possível completar a criação do torneio"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }












    fun entryTournament(entry: Entry){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.entryTournament(entry)
            Log.e("getTournamentsJoinedByUser", result.toString())
            result.fold(
                onSuccess = {
                    _entry.value = it
                    _appState.value = DataState.Success
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Não foi possível completar a operação"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }

}