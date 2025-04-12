package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.CreateRequest
import com.draccoapp.poker.api.model.request.Login
import com.draccoapp.poker.api.model.request.Login2FARequest
import com.draccoapp.poker.api.model.request.Login2faBodyNew
import com.draccoapp.poker.api.model.request.ValidateCode
import com.draccoapp.poker.api.model.request.ValidateFieldsRequest
import com.draccoapp.poker.api.model.response.CreateResponse
import com.draccoapp.poker.api.model.response.Login2FAResponse
import com.draccoapp.poker.api.model.response.Login2faResponseNew
import com.draccoapp.poker.api.model.response.LoginResponse
import com.draccoapp.poker.api.model.response.ValidateCodeResponse
import com.draccoapp.poker.api.model.response.ValidateFieldsResponse
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.repository.AuthRepository
import com.draccoapp.poker.repository.GlobalRepository
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.limparMessage
import com.draccoapp.poker.utils.mostrarToast
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AuthViewModel(
    private val repository: AuthRepository,
    private val globalRepository: GlobalRepository,
    private val preferences: Preferences
) : ViewModel() {

    private val _error400 = MutableLiveData<Boolean>()
    val error400: LiveData<Boolean> = _error400

    private val _error401 = MutableLiveData<Boolean>()
    val error401: LiveData<Boolean> = _error401

    val error: LiveData<String>
        get() = _error

    private val _error = MutableLiveData<String>()

    val appState: LiveData<DataState>
        get() = _appState

    private val _appState = MutableLiveData<DataState>()

    val successLogin2fa = MutableLiveData<Login2faResponseNew>()

    val successLogin: LiveData<LoginResponse>
        get() = _successLogin

    private val _successLogin = MutableLiveData<LoginResponse>()

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

    private fun saveToken(token: Login2FAResponse) {
//        preferences.saveToken(token)
    }

    private fun saveKey(key: String) {
//        preferences.saveToken(key)
    }

    fun getKey(): String {
        return preferences.getUserId()
    }

    fun getIsLogged(): Boolean {
        return preferences.getToken().isNotEmpty()
    }

    fun login(body: Login) {
        repository.login(body).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _successLogin.postValue(it)
                    }
                    Log.i("CadastroViewModel", "onResponse: A resposta foi isSuccessfull")
                } else {
                    // Verifica o código de status HTTP
                    when (response.code()) {
                        400 -> {
                            _error400.postValue(true)
                            try {
                                val errorBody = response.errorBody()?.string()
                                val erroLoginLimpo = limparMessage(errorBody.toString())
                                Log.e(
                                    "Error Body",
                                    "O erro do servidor foi ${erroLoginLimpo ?: "erro desconhecido"} "
                                )
                                mostrarToast("$erroLoginLimpo", PokerApplication.instance)
                            } catch (e: IOException) {
                                Log.e("IOException", "Erro de leitura do response ->>", e)
                            }
                        }

                        401 -> {
                            _error401.postValue(true)
                        }

                        else -> {
                            mostrarToast(
                                "Erro de código ${response.code()}",
                                PokerApplication.instance
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e(
                    "CadastroViewModel",
                    "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi \n$t"
                )
            }

        })

    }

    fun getHomeFragment(onSessionCreated: () -> Unit) {
        globalRepository.getHomeFragment().enqueue(object : Callback<HomeFragmentResponse> {
            override fun onResponse(
                call: Call<HomeFragmentResponse>,
                response: Response<HomeFragmentResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { preferences.createSession(it.myself, onSessionCreated) }
                } else {
                    Log.e("HomeViewModel", "Erro MySelf ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HomeFragmentResponse>, t: Throwable) {
                Log.e("HomeViewModel", "ONFAILUREEE  o erro na função getMeusDados foi $t")
            }
        })
    }

    fun login2faNew(body: Login2faBodyNew) {
        repository.login2faNew(body).enqueue(object : Callback<Login2faResponseNew> {
            override fun onResponse(
                call: Call<Login2faResponseNew>,
                response: Response<Login2faResponseNew>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        preferences.saveToken(it)
                        getHomeFragment(){
                            successLogin2fa.postValue(it)
                        }
                    }
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        Log.e(
                            "Error Body",
                            "O erro  do servidor  LIMPOOO  foi ${erroLoginLimpo ?: "erro desconhecido"} "
                        )
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }
            }

            override fun onFailure(call: Call<Login2faResponseNew>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e(
                    "AuthViewModel",
                    "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t"
                )
            }

        })

    }


    fun login2fa(login: Login2FARequest) {
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.login2fa(login)

            result.fold(
                onSuccess = {
                    _login2fa.value = it
                    _appState.value = DataState.Success
                    it?.let { accessToken ->
                        saveToken(accessToken)
                        Log.i(
                            "PrefsToken",
                            "login2fa: o token q ta sendo salvo em preferences é $accessToken "
                        )
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

    fun validateFields(body: ValidateFieldsRequest) {
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

    fun validateCode(body: ValidateCode) {
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

    fun create(body: CreateRequest) {
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