package com.nitesh.findingfalcone.domain

import com.nitesh.findingfalcone.domain.model.FindResponse
import com.nitesh.findingfalcone.domain.model.Planet
import com.nitesh.findingfalcone.domain.model.Vehicle
import io.reactivex.Completable
import io.reactivex.Single

interface Repository {
    fun getToken(): Completable

    fun getPlanets(): Single<List<Planet>>
    fun getVehicles(): Single<List<Vehicle>>

    fun findPrinces(planets: List<Planet>, vehicles: List<Vehicle>): Single<FindResponse>
}