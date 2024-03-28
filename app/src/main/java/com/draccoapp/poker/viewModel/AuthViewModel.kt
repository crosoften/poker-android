package com.draccoapp.poker.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.repository.AuthRepository
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val preferences: Preferences
): ViewModel() {

    val error: LiveData<String>
        get() = _error

    private val _error = MutableLiveData<String>()

    val appState: LiveData<DataState>
        get() = _appState

    private val _appState = MutableLiveData<DataState>()

    val login: LiveData<LoginResponse>
        get() = _login

    private val _login = MutableLiveData<LoginResponse>()

    private fun saveToken(token: LoginResponse){
        preferences.saveToken(token)
    }

    fun getIsLogged(): Boolean {
        return preferences.getToken().isNotEmpty()
    }


    fun login(login: Login){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.login(login)

            result.fold(
                onSuccess = {
                    _login.value = it
                    _appState.value = DataState.Success
                    it?.let { token ->
                        saveToken(token)
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

}