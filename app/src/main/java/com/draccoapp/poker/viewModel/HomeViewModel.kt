package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.draccoapp.poker.api.model.response.MeusDadosResponse
import com.draccoapp.poker.api.model.response.homeFrament.HomeFragmentResponse
import com.draccoapp.poker.api.service.chatSocket.ChatSocketService
import com.draccoapp.poker.repository.GlobalRepository
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.limparMessage
import com.draccoapp.poker.utils.mostrarToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeViewModel(private val repository: GlobalRepository) : ViewModel() {

    val successMeusDados = MutableLiveData<MeusDadosResponse>()
    val successHomeFragment = MutableLiveData<HomeFragmentResponse>()

    fun getHomeFragment() {
        repository.getHomeFragment().enqueue(object : Callback<HomeFragmentResponse> {
            override fun onResponse(call: Call<HomeFragmentResponse>, response: Response<HomeFragmentResponse>) {
                if (response.isSuccessful) {
                    successHomeFragment.postValue(response.body())
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                    }
                }
            }

            override fun onFailure(call: Call<HomeFragmentResponse>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("HomeViewModel", "ONFAILUREEE  o erro na função getMeusDados foi $t")
            }

        })
    }

    fun getMeusDados() {
        repository.getMeusDados().enqueue(object : Callback<MeusDadosResponse> {
            override fun onResponse(call: Call<MeusDadosResponse>, response: Response<MeusDadosResponse>) {
                if (response.isSuccessful) {
                    successMeusDados.postValue(response.body())
                    Log.i("HomeViewModel", "onResponse: A resposta foi isSuccessfull")
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

            override fun onFailure(call: Call<MeusDadosResponse>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("HomeViewModel", "ONFAILUREEE  o erro na função getMeusDados foi $t")
            }

        })
    }


}