package com.draccoapp.poker.api.model.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable
import java.time.LocalDate

@JsonClass(generateAdapter = true)
data class Tournament(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String?,
    @Json(name = "description")
    val description: String,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "date")
    val date: String,
    @Json(name = "latitude")
    val latitude: Double,
    @Json(name = "longitude")
    val longitude: Double,
    @Json(name = "imageURL")
    val imageURL: String,
    @Json(name = "distance")
    val distance: Double?
) : Serializable

fun generateTournaments(): List<Tournament> {
    val tournaments = mutableListOf<Tournament>()
    for (i in 1..10) {
        val tournament = Tournament(
            id = i,
            name = "Tournament $i",
            description = "Description for Tournament $i",
            prize = i * 1000,
            date = LocalDate.now().plusDays(i.toLong()).toString(),
            latitude = -23.550520,
            longitude = -46.633308,
            imageURL = "https://example.com/image$i.jpg",
            distance = i.toDouble()
        )
        tournaments.add(tournament)
    }
    return tournaments
}