package com.draccoapp.poker.api.model.response.homeFrament


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NextTournament(
    @Json(name = "description")
    val description: String?,
    @Json(name = "eventUrl")
    val eventUrl: String?,
    @Json(name = "finalDatetime")
    val finalDatetime: String?,
    @Json(name = "formId")
    val formId: String?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "imageUrl")
    val imageUrl: String?,
    @Json(name = "location")
    val location: LocationX?,
    @Json(name = "prize")
    val prize: Int,
    @Json(name = "rules")
    val rules: String?,
    @Json(name = "startDatetime")
    val startDatetime: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "time")
    val time: String?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "type")
    val type: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        TODO("location"),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeString(eventUrl)
        parcel.writeString(finalDatetime)
        parcel.writeString(formId)
        parcel.writeString(id)
        parcel.writeString(imageUrl)
        parcel.writeInt(prize)
        parcel.writeString(rules)
        parcel.writeString(startDatetime)
        parcel.writeString(status)
        parcel.writeString(time)
        parcel.writeString(title)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NextTournament> {
        override fun createFromParcel(parcel: Parcel): NextTournament {
            return NextTournament(parcel)
        }

        override fun newArray(size: Int): Array<NextTournament?> {
            return arrayOfNulls(size)
        }
    }
}