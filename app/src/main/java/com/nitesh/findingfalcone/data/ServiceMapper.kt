package com.nitesh.findingfalcone.data

import com.nitesh.findingfalcone.data.model.FindApiResponse
import com.nitesh.findingfalcone.data.model.PlanetsApiResponse
import com.nitesh.findingfalcone.data.model.VehiclesApiResponse
import com.nitesh.findingfalcone.domain.model.FindResponse
import com.nitesh.findingfalcone.domain.model.Planet
import com.nitesh.findingfalcone.domain.model.Vehicle

fun PlanetsApiResponse.mapToPlanet() = Planet(name, distance)

fun VehiclesApiResponse.mapToVehicle() = Vehicle(name, amount, maxDistance, speed)

fun FindApiResponse.mapToFindResponse() =
    when (status) {
        "success" -> FindResponse.Success(planetName!!)
        "false" -> FindResponse.Failure
        else -> throw SecurityException(error)
    }