package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateResponse(
    @Json(name = "id")
    val id: String
)