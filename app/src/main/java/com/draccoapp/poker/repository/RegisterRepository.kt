package com.draccoapp.poker.repository

import com.draccoapp.poker.api.model.request.RegisterStep1Body
import com.draccoapp.poker.api.service.RegisterService
import okhttp3.MultipartBody

class RegisterRepository(private val service: RegisterService) {

    fun uploadFiles(file: MultipartBody.Part?) = service.uploadArquivos(file)

    fun registerStep1(body: RegisterStep1Body) = service.registerStep1(body)

}