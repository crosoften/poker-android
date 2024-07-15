package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.CreateRequest
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.Login2FARequest
import com.draccoapp.poker.api.model.request.ValidateCode
import com.draccoapp.poker.api.model.request.ValidateFieldsRequest
import com.draccoapp.poker.api.model.response.CreateResponse
import com.draccoapp.poker.api.model.response.Login2FAResponse
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.response.ValidateCodeResponse
import com.draccoapp.poker.api.model.response.ValidateFieldsResponse
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

    val login2fa: LiveData<Login2FAResponse>
        get() = _login2fa

    private val _login2fa = MutableLiveData<Login2FAResponse>()

    val validateFields: LiveData<ValidateFieldsResponse>
        get() = _validateFields

    private val _validateFields = MutableLiveData<ValidateFieldsResponse>()

    val validateCode: LiveData<ValidateCodeResponse>
        get() = _validateCode

    private val _validateCode = MutableLiveData<ValidateCodeResponse>()

    val create: LiveData<CreateResponse>
        get() = _create

    private val _create = MutableLiveData<CreateResponse>()

    private fun saveToken(token: Login2FAResponse){
        preferences.saveToken(token)
    }

    private fun saveKey(key: LoginResponse){
        preferences.saveID(key)
    }

    fun getKey(): String {
        return preferences.getUserId()
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
                    it?.let { id ->
                        saveKey(id)
                    }
                },

                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                        Log.i("WillErro", "login: O erro foi  $e")
                    } ?: run {
                        _error.value = "Oopss.. algo deu errado"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }

    fun login2fa(login: Login2FARequest){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.login2fa(login)

            result.fold(
                onSuccess = {
                    _login2fa.value = it
                    _appState.value = DataState.Success
                    it?.let { accessToken ->
                        saveToken(accessToken)
                    }
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Oopss.. algo deu errado"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }

    fun validateFields(body: ValidateFieldsRequest){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.validateFields(body)

            result.fold(
                onSuccess = {
                    _validateFields.value = it
                    _appState.value = DataState.Success
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Oopss.. algo deu errado"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }

    fun validateCode(body: ValidateCode){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.validateCode(body)

            result.fold(
                onSuccess = {
                    _validateCode.value = it
                    _appState.value = DataState.Success
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Oopss.. algo deu errado"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }

    fun create(body: CreateRequest){
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.create(body)

            result.fold(
                onSuccess = {
                    _create.value = it
                    _appState.value = DataState.Success
                },
                onFailure = {
                    it.message?.let { e ->
                        _error.value = e
                    } ?: run {
                        _error.value = "Oopss.. algo deu errado"
                    }
                    _appState.value = DataState.Error
                }
            )
        }
    }



}