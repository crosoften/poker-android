package com.draccoapp.poker.api.model.response.contract.details

data class Player(
    val birthday: String,
    val email: String,
    val gender: String,
    val id: String,
    val imageUrl: String,
    val location: Location,
    val name: String,
    val phone: String,
    val reprovedDueTo: String?,
    val status: String
)