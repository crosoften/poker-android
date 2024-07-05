package com.draccoapp.poker.api.service

import com.draccoapp.poker.api.model.request.RegisterStep1Body
import com.draccoapp.poker.api.model.response.RegisterStep1Response
import com.draccoapp.poker.api.model.response.UploadFileResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RegisterService {

    @Multipart
    @POST("/upload-file")
    fun uploadArquivos(@Part file: MultipartBody.Part?) : Call<UploadFileResponse>


    @POST("/accounts/validate-fields")
    fun registerStep1(@Body body : RegisterStep1Body) : Call<RegisterStep1Response>


}