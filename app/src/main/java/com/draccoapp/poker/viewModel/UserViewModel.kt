package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.Entry
import com.draccoapp.poker.api.model.request.UpdateLocation
import com.draccoapp.poker.api.model.response.ApplicanteTournamentResponse
import com.draccoapp.poker.api.model.response.Tournament
import com.draccoapp.poker.api.model.response.TournamentResponse
import com.draccoapp.poker.api.model.response.User
import com.draccoapp.poker.api.model.response.generateTournaments
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.repository.TournamentRepository
import com.draccoapp.poker.repository.UserRepository
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository,
    private val repositoryTournament: TournamentRepository,
    private val preferences: Preferences
) : ViewModel() {

    val error: LiveData<String>
        get() = _error

    private val _error = MutableLiveData<String>()

    val appState: LiveData<DataState>
        get() = _appState

    private val _appState = MutableLiveData<DataState>()

    val user: LiveData<User>
        get() = _user

    private val _user = MutableLiveData<User>()

    val tournamentsByUser: LiveData<List<Tournament>>
        get() = _tournamentNext

    private val _tournamentNext = MutableLiveData<List<Tournament>>()

    val tournamentApplicant: LiveData<List<Tournament>>
        get() = _tournamentApplicant

    private val _tournamentApplicant = MutableLiveData<List<Tournament>>()

    val updateLocation: LiveData<Unit>
        get() = _updateLocation

    private val _updateLocation = MutableLiveData<Unit>()

    private fun saveUser(user: User){
        preferences.saveUser(user)
    }

    init {
        _tournamentApplicant.value = generateTournaments()
        _tournamentNext.value = generateTournaments()
    }

    fun getUserById(){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.getUserById(preferences.getUserId())

            result.fold(
                onSuccess = {
                    _user.value = it
                    _appState.value = DataState.Success
                    it?.let {
                        saveUser(it)
                    }
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

    fun getTournamentsAvailableToUser(){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repositoryTournament.getTournamentsAvailableToUser(preferences.getUserId())
            Log.e("getTournamentsAvailableToUser", result.toString())
            result.fold(
                onSuccess = {
//                    _tournamentNext.value = it
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

    fun getTournamentsJoinedByUser(status: String? = null){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repositoryTournament.getTournamentsJoinedByUser(preferences.getUserId(), status)
            Log.e("getTournamentsJoinedByUser", result.toString())
            result.fold(
                onSuccess = {
//                    _tournamentApplicant.value = it
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

    fun getUnit(): String {
        return preferences.getUnit()
    }

    fun updateLocation(update: UpdateLocation){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.updateLocation(preferences.getUserId(), update)
            Log.e("updateLocation", result.toString())
            result.fold(
                onSuccess = {
                    _updateLocation.value = it
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