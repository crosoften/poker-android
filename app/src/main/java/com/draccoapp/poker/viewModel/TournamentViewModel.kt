package com.draccoapp.poker.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.draccoapp.poker.api.model.request.AddUpdadeTournament
import com.draccoapp.poker.api.model.request.AnswerBody
import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.api.model.response.AnswerResponse
import com.draccoapp.poker.api.model.response.DetailsUpdateTournament
import com.draccoapp.poker.api.model.response.TournamentResponseNew
import com.draccoapp.poker.api.model.response.UploadFileResponse
import com.draccoapp.poker.api.model.response.tournamentForms.TournamentForms
import com.draccoapp.poker.api.model.response.tournamentInIm.TournamentInImResponse
import com.draccoapp.poker.api.model.response.updateTournament.UpdateTournament
import com.draccoapp.poker.api.modelOld.request.Entry
import com.draccoapp.poker.api.model.type.DataState
import com.draccoapp.poker.repository.TournamentRepository
import com.draccoapp.poker.utils.PokerApplication
import com.draccoapp.poker.utils.Preferences
import com.draccoapp.poker.utils.ResponseParser
import com.draccoapp.poker.utils.limparMessage
import com.draccoapp.poker.utils.mostrarToast
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import java.io.IOException

class TournamentViewModel(
    private val repository: TournamentRepository,
    private val preferences: Preferences
) : ViewModel() {

    val successCreateTournament = MutableLiveData<TournamentResponseNew>()
    val successUploadFile = MutableLiveData<UploadFileResponse>()
    val successGetTournamentForms = MutableLiveData<TournamentForms>()
    val successSubscribeTournament = MutableLiveData<AnswerResponse>()
    val successUpdateTournament = MutableLiveData<UpdateTournament>()
    val successTournamentInIm = MutableLiveData<TournamentInImResponse>()
    val successAddUpdate = MutableLiveData<AnswerResponse>()
    val successGetDetailsUpdadte = MutableLiveData<DetailsUpdateTournament>()

    val error: LiveData<String>
        get() = _error

    private val _error = MutableLiveData<String>()

    val appState: LiveData<DataState>
        get() = _appState

    private val _appState = MutableLiveData<DataState>()

    val entry: LiveData<Unit>
        get() = _entry

    private val _entry = MutableLiveData<Unit>()


fun subscribeToTournament(idTounament : String, answerBody: AnswerBody){
    repository.subscribeToTournament(idTournament = idTounament, answerBody = answerBody).enqueue(object : Callback<AnswerResponse> {
        override fun onResponse(call: Call<AnswerResponse>, response: Response<AnswerResponse>) {
            if (response.isSuccessful) {
                successSubscribeTournament.postValue(response.body())
                Log.i("TournamentViewModel", "onResponse: A resposta foi isSuccessfull")
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

        override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
            mostrarToast("Generic error", PokerApplication.instance)
            Log.e("TournamentViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
        }

    })

}

    fun getFavorites(subscriptionId: String) {
        val request = repository.getUpdate(subscriptionId)
        request.enqueue(object : Callback<UpdateTournament> {
            override fun onResponse(
                call: Call<UpdateTournament>,
                response: Response<UpdateTournament>
            ) {
                if (response.isSuccessful) {
                    successUpdateTournament.postValue(response.body())
                } else {
                    _error.postValue(ResponseParser.parseError(response))
                }
            }

            override fun onFailure(call: Call<UpdateTournament>, t: Throwable) {
                _error.postValue(t.message)
            }

        })

    }

    fun getTounamentImIn() {
        val request = repository.getTounamentImIn()
        request.enqueue(object : Callback<TournamentInImResponse> {
            override fun onResponse(
                call: Call<TournamentInImResponse>,
                response: Response<TournamentInImResponse>
            ) {
                if (response.isSuccessful) {
                    successTournamentInIm.postValue(response.body())
                } else {
                    _error.postValue(ResponseParser.parseError(response))
                }
            }

            override fun onFailure(call: Call<TournamentInImResponse>, t: Throwable) {
                _error.postValue(t.message)
            }

        })

    }

    fun createUpdate(body: AddUpdadeTournament) {
        val request = repository.createUpdate(body)
        request.enqueue(object : Callback<AnswerResponse> {
            override fun onResponse(
                call: Call<AnswerResponse>,
                response: Response<AnswerResponse>
            ) {
                if (response.isSuccessful) {
                    successAddUpdate.postValue(response.body())
                } else {
                    _error.postValue(ResponseParser.parseError(response))
                }
            }

            override fun onFailure(call: Call<AnswerResponse>, t: Throwable) {
                _error.postValue(t.message)
            }

        })

    }
fun getUpdateDetails(id: String) {
        val request = repository.getUpdateDetails(id)
        request.enqueue(object : Callback<DetailsUpdateTournament> {
            override fun onResponse(
                call: Call<DetailsUpdateTournament>,
                response: Response<DetailsUpdateTournament>
            ) {
                if (response.isSuccessful) {
                    successGetDetailsUpdadte.postValue(response.body())
                } else {
                    _error.postValue(ResponseParser.parseError(response))
                }
            }

            override fun onFailure(call: Call<DetailsUpdateTournament>, t: Throwable) {
                _error.postValue(t.message)
            }

        })

    }


    fun getTournament(id: String) {
        _appState.postValue(DataState.Loading)
        repository.getTournament(id).enqueue(object : Callback<TournamentForms> {
            override fun onResponse(call: Call<TournamentForms>, response: Response<TournamentForms>) {
                if (response.isSuccessful) {
                    successGetTournamentForms.postValue(response.body())
                    _appState.postValue(DataState.Success)
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        val erroLoginLimpo = limparMessage(errorBody.toString())
                        mostrarToast(" $erroLoginLimpo ", PokerApplication.instance)
                        _appState.postValue(DataState.Error)
                    } catch (e: IOException) {
                        Log.e("IOException", "Erro de leitura do response ->>", e)
                        _appState.postValue(DataState.Error)
                    }
                }
            }

            override fun onFailure(call: Call<TournamentForms>, t: Throwable) {
                mostrarToast("Generic error", PokerApplication.instance)
                Log.e("TournamentViewModel", "ONFAILUREEE  o erro na função solicitarTokenViewModel do CadastroViewModel foi $t")
            }

        })
    }



    fun createTournament(body: TournamentBodyNew) {
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

    fun uploadFile(file: MultipartBody.Part?) {
        _appState.postValue(DataState.Loading)
        viewModelScope.launch {
            val result = repository.uploadFile(file)
            Log.e("TournamentViewModel", "O result da criação do torneio foi : $result")
            result.fold(
                onSuccess = {
                    _appState.value = DataState.Success
                    successUploadFile.value = it
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


    fun entryTournament(entry: Entry) {
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


    val listaDeEstadosBrasileiros = mutableListOf(
        "Acre",
        "Alagoas",
        "Amapá",
        "Amazonas",
        "Bahia",
        "Ceará",
        "Distrito Federal",
        "Espírito Santo",
        "Goiás",
        "Maranhão",
        "Mato Grosso",
        "Mato Grosso do Sul",
        "Minas Gerais",
        "Pará",
        "Paraíba",
        "Paraná",
        "Pernambuco",
        "Piauí",
        "Rio de Janeiro",
        "Rio Grande do Norte",
        "Rio Grande do Sul",
        "Rondônia",
        "Roraima",
        "Santa Catarina",
        "São Paulo",
        "Sergipe",
        "Tocantins"
    )

    val listaDeEstadosEUA = mutableListOf(
        "Alabama",
        "Alaska",
        "Arizona",
        "Arkansas",
        "California",
        "Colorado",
        "Connecticut",
        "Delaware",
        "Florida",
        "Georgia",
        "Hawaii",
        "Idaho",
        "Illinois",
        "Indiana",
        "Iowa",
        "Kansas",
        "Kentucky",
        "Louisiana",
        "Maine",
        "Maryland",
        "Massachusetts",
        "Michigan",
        "Minnesota",
        "Mississippi",
        "Missouri",
        "Montana",
        "Nebraska",
        "Nevada",
        "New Hampshire",
        "New Jersey",
        "New Mexico",
        "New York",
        "North Carolina",
        "North Dakota",
        "Ohio",
        "Oklahoma",
        "Oregon",
        "Pennsylvania",
        "Rhode Island",
        "South Carolina",
        "South Dakota",
        "Tennessee",
        "Texas",
        "Utah",
        "Vermont",
        "Virginia",
        "Washington",
        "West Virginia",
        "Wisconsin",
        "Wyoming"
    )
}