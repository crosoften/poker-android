package com.draccoapp.poker.api.model.response.contract


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ContractResponse(
    @Json(name = "currentPage")
    val currentPage: Int,
    @Json(name = "data")
    val `data`: List<Contract>,
    @Json(name = "itemsPerPage")
    val itemsPerPage: Int,
    @Json(name = "totalItems")
    val totalItems: Int,
    @Json(name = "totalPages")
    val totalPages: Int
)