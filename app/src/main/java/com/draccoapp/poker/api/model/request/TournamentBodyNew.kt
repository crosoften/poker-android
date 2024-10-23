package com.draccoapp.poker.api.model.request


import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TournamentBodyNew(
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("prize")
    val prize: Int?,
    @SerializedName("startDatetime")
    val startDatetime: String?,
    @SerializedName("finalDatetime")
    val finalDatetime: String?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("eventUrl")
    val eventUrl: String?,
    @SerializedName("proofUrl")
    val proofUrl: String?,
    @SerializedName("zipCode")
    val zipCode: String?,
    @SerializedName("street")
    val street: String?,
    @SerializedName("number")
    val number: String?,
    @SerializedName("neighborhood")
    val neighborhood: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("lat")
    val lat: String? = "-48.215536",
    @SerializedName("lng")
    val lng: String? = "-18.215536"
)