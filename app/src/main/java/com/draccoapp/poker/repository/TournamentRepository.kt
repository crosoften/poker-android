package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.request.AddUpdadeTournament
import com.draccoapp.poker.api.model.request.AnswerBody
import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.api.model.response.TournamentResponseNew
import com.draccoapp.poker.api.model.response.UploadFileResponse
import com.draccoapp.poker.api.modelOld.request.Entry
import com.draccoapp.poker.api.modelOld.response.ApplicanteTournamentResponse
import com.draccoapp.poker.api.modelOld.response.TournamentResponse
import com.draccoapp.poker.api.service.TournamentService
import com.draccoapp.poker.utils.Preferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.lang.Exception

class TournamentRepository(
    private val service: TournamentService,
    private val preferences: Preferences,
    private val dispatcher: CoroutineDispatcher
) {


    fun getTournament(id: String) = service.getTournament(id)
    fun getUpdateDetails(id: String) = service.getUpdateDetails(id)
    fun createUpdate(body: AddUpdadeTournament) = service.createUpdate(body)

    fun subscribeToTournament(idTournament: String, answerBody: AnswerBody) = service.subscribeToTournament(idTournament, answerBody)


    suspend fun createTournament(body: TournamentBodyNew): Result<TournamentResponseNew?> =
        withContext(dispatcher) {
            try {
                val response = service.createTournament(
                    body = body
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }


    suspend fun uploadFile (file: MultipartBody.Part?) : Result<UploadFileResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.uploadArquivos(file)
                Result.success(response)

//                when {
//                    response.isSuccessful -> {
//                        Result.success(response.body())
//                    }
//
//                    else -> {
//                        Result.failure(Throwable(response.message()))
//                    }
//                }

            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }


    suspend fun getTournamentsAvailableToUser(userId: Int): Result<TournamentResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.getTournamentsAvailableToUser(
                    userId = userId
                )
                when {
                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun getTournamentsJoinedByUser(userId: Int, status: String?): Result<ApplicanteTournamentResponse?> =
        withContext(dispatcher) {
            try {
                val response = service.getTournamentsJoinedByUser(
                    userId = userId,
                    status = status
                )
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }

    suspend fun entryTournament(entry: Entry): Result<Unit?> =
        withContext(dispatcher) {
            try {
                val response = service.entryTournament(entry)
                when {

                    response.isSuccessful -> {
                        Result.success(response.body())
                    }

                    else -> {
                        Result.failure(Throwable(response.message()))
                    }
                }
            } catch (e: Exception) {
                Result.failure(Throwable(e.message))
            }
        }


    fun getUpdate(subscriptionId: String) = service.getUpdate(subscriptionId)
    fun getTounamentImIn(
    ) = service.getTounamentImIn(
        lat = preferences.getLatitude(),
        lng = preferences.getLongitude()
    )

}