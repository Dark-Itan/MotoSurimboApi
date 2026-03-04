package com.motosurimbo.api.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Unit(
    val id: Int = 0,
    val unitNumber: String,
    val driverName: String,
    val licensePlate: String,
    val status: String,
    val shift: String
)

@Serializable
data class CreateUnitRequest(
    val unitNumber: String = "",
    val driverName: String = "",
    val licensePlate: String = "",
    val status: String = "",
    val shift: String = ""
)


@Serializable
data class UpdateUnitRequest(
    val unitNumber: String,
    val driverName: String,
    val licensePlate: String,
    val status: String,
    val shift: String
)