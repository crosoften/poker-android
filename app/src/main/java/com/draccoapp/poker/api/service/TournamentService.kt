package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.TournamentBodyNew
import com.draccoapp.poker.api.model.response.TournamentResponseNew
import com.draccoapp.poker.api.model.response.UploadFileResponse
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


    @POST("tournaments/non-official")
    suspend fun createTournament(
        @Body body: TournamentBodyNew
    ): Response<TournamentResponseNew>

    @Multipart
    @POST("/upload-file")
    suspend fun uploadArquivos(@Part file: MultipartBody.Part?) : UploadFileResponse


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
}