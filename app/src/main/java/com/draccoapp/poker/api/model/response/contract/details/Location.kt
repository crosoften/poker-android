package com.draccoapp.poker.api.model.response.contract.details

data class Location(
    val city: String,
    val lat: Double?,
    val lng: Double?,
    val state: String
)