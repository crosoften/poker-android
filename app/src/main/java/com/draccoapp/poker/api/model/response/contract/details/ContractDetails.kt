package com.draccoapp.poker.api.model.response.contract.details

data class ContractDetails(
    val endDate: String,
    val fileId: String,
    val id: String,
    val player: Player,
    val playerId: String,
    val playerSignature: Boolean,
    val startDate: String,
    val status: String,
    val title: String
)