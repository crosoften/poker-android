package com.draccoapp.poker.viewModel

import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.draccoapp.poker.R
import com.draccoapp.poker.api.model.request.RegisterStep1Body
import com.draccoapp.poker.api.model.response.RegisterStep1Response
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
    val successRegisterStep1 = MutableLiveData<RegisterStep1Response>()


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
        repository.registerStep1(body).enqueue(object : Callback<RegisterStep1Response> {
            override fun onResponse(call: Call<RegisterStep1Response>, response: Response<RegisterStep1Response>) {
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

            override fun onFailure(call: Call<RegisterStep1Response>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("CadastroViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }
        })
    }


}