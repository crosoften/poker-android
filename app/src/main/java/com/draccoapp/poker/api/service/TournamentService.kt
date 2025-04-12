package com.draccoapp.poker.api.service

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
import com.draccoapp.poker.api.modelOld.response.ApplicanteTournamentResponse
import com.draccoapp.poker.api.modelOld.response.Tournament
import com.draccoapp.poker.api.modelOld.response.TournamentResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface TournamentService {


    @GET("tournaments/{id}")
    fun getTournament(@Path("id") id: String): Call<TournamentForms>

    @POST("tournaments/non-official")
    suspend fun createTournament(
        @Body body: TournamentBodyNew
    ): Response<TournamentResponseNew>

    @Multipart
    @POST("/upload-file")
    suspend fun uploadArquivos(@Part file: MultipartBody.Part?): UploadFileResponse

    @POST("/tournaments/{idTournament}/subscribe")
    fun subscribeToTournament(
        @Path("idTournament") idTournament: String,
        @Body answerBody: AnswerBody
    ): Call<AnswerResponse>

//    @Multipart
//    @POST("tournament")
//    suspend fun createTournament(
//    ): Response<Void>

    @GET("tournament/{tournamentId}")
    suspend fun getTournamentById(
        @Path("tournamentId") tournamentId: String
    ): Response<Tournament>

    @GET("tournament/availableTo/{userId}")
    suspend fun getTournamentsAvailableToUser(
        @Path("userId") userId: Int,
        @Query("page") page: Int = 1,
    ): Response<TournamentResponse>

    @GET("/tournament/entry/joinedBy/{userId}")
    suspend fun getTournamentsJoinedByUser(
        @Path("userId") userId: Int,
        @Query("state") status: String?,
        @Query("page") page: Int = 1,
    ): Response<ApplicanteTournamentResponse>

    @POST("/tournament/entry")
    suspend fun entryTournament(
        @Body entry: Entry
    ): Response<Unit>

    @GET("/rankings")
    fun getUpdate(
        @Query("subscriptionId") subscriptionId: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100,
    ): Call<UpdateTournament>

    @GET("/accounts/myself/subscriptions")
    fun getTounamentImIn(
        @Query("lat") lat: String,
        @Query("lng") lng: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100,
    ): Call<TournamentInImResponse>

    @POST("/rankings")
    fun createUpdate(
     @Body body: AddUpdadeTournament
    ): Call<AnswerResponse>

    @GET("/rankings/{id}")
    fun getUpdateDetails(
        @Path("id") id: String,
    ): Call<DetailsUpdateTournament>

}