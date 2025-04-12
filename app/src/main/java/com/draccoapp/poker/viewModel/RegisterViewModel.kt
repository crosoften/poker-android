package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.draccoapp.poker.api.model.request.RegisterStep1Body
import com.draccoapp.poker.api.model.request.RegisterStep2Body
import com.draccoapp.poker.api.model.request.RegisterStep3Body
import com.draccoapp.poker.api.model.response.RegisterStep1e2Response
import com.draccoapp.poker.api.model.response.RegisterStep3Response
import com.draccoapp.poker.api.model.response.UploadFileResponse
import com.draccoapp.poker.repository.RegisterRepository
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.limparMessage
import com.draccoapp.poker.utils.mostrarToast
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RegisterViewModel(
    private val repository: RegisterRepository
) : ViewModel() {

    val successUpload = MutableLiveData<UploadFileResponse>()
    val successRegisterStep1 = MutableLiveData<RegisterStep1e2Response>()
    val successRegisterStep2 = MutableLiveData<RegisterStep1e2Response>()
    val successRegisterStep3 = MutableLiveData<RegisterStep3Response>()


    fun uploadArquivo(file: MultipartBody.Part?) {
        repository.uploadFiles(file).enqueue(object : Callback<UploadFileResponse> {
            override fun onResponse(call: Call<UploadFileResponse>, response: Response<UploadFileResponse>) {
                if (response.isSuccessful) {
                    successUpload.postValue(response.body())
                    Log.i("CadastroViewModel", "onResponse: A resposta foi isSuccessfull")
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        Log.e("Error Body", "O erro  do servidor  LIMPOOO  foi ${erroLoginLimpo ?: "erro desconhecido"} ")
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }
            }

            override fun onFailure(call: Call<UploadFileResponse>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("CadastroViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }
        })
    }

    fun registerStep1(body: RegisterStep1Body) {
        repository.registerStep1(body).enqueue(object : Callback<RegisterStep1e2Response> {
            override fun onResponse(call: Call<RegisterStep1e2Response>, response: Response<RegisterStep1e2Response>) {
                if (response.isSuccessful) {
                    successRegisterStep1.postValue(response.body())
                    Log.i("CadastroViewModel", "onResponse: A resposta foi isSuccessfull")
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        Log.e("Error Body", "O erro  do servidor  LIMPOOO  foi ${erroLoginLimpo ?: "erro desconhecido"} ")
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }

            }

            override fun onFailure(call: Call<RegisterStep1e2Response>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("CadastroViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }
        })
    }


    fun registerStep2(body: RegisterStep2Body){
        repository.registerStep2(body).enqueue(object  : Callback<RegisterStep1e2Response>{
            override fun onResponse(call: Call<RegisterStep1e2Response>, response: Response<RegisterStep1e2Response>) {
                if (response.isSuccessful) {
                    successRegisterStep2.postValue(response.body())
                    Log.i("CadastroViewModel", "onResponse: A resposta foi isSuccessfull")
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        Log.e("Error Body", "O erro  do servidor  LIMPOOO  foi ${erroLoginLimpo ?: "erro desconhecido"} ")
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }
            }

            override fun onFailure(call: Call<RegisterStep1e2Response>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("CadastroViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }

        })
    }


    fun registerStep3(body: RegisterStep3Body){
        repository.registerStep3(body).enqueue(object : Callback<RegisterStep3Response>{
            override fun onResponse(call: Call<RegisterStep3Response>, response: Response<RegisterStep3Response>) {
                if (response.isSuccessful) {
                    successRegisterStep3.postValue(response.body())
                    Log.i("CadastroViewModel", "onResponse: A resposta foi isSuccessfull")
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        Log.e("Error Body", "O erro  do servidor  LIMPOOO  foi ${erroLoginLimpo ?: "erro desconhecido"} ")
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }
            }

            override fun onFailure(call: Call<RegisterStep3Response>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("CadastroViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }
        })
    }


}