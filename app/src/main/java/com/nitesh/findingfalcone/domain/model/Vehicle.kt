package com.nitesh.findingfalcone.domain.model

import android.os.Parcelable
import com.nitesh.findingfalcone.R
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Vehicle(
    val name: String,
    val amount: Int,
    val maxDistance: Int,
    val speed: Int
) : Parcelable {
    fun getImage() = when (name) {
        "Space pod" -> R.drawable.ic_space_pod
        "Space rocket" -> R.drawable.ic_space_rocket
        "Space shuttle" -> R.drawable.ic_space_shuttle
        "Space ship" -> R.drawable.ic_space_ship
        else -> throw NoSuchElementException("unknown vehicle")
    }
}