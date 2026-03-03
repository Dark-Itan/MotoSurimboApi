package com.motosurimbo.api.domain.services

import com.motosurimbo.api.domain.models.Unit
import com.motosurimbo.api.domain.models.CreateUnitRequest
import com.motosurimbo.api.domain.models.UpdateUnitRequest
import com.motosurimbo.api.domain.repositories.UnitRepository

class UnitService(
    private val unitRepository: UnitRepository
) {
    suspend fun getAllUnits(): List<Unit> = unitRepository.getAllUnits()

    suspend fun getUnitById(id: Int): Unit? = unitRepository.getUnitById(id)

    suspend fun createUnit(request: CreateUnitRequest): Unit {
        val unit = Unit(
            unitNumber = request.unitNumber,
            driverName = request.driverName,
            licensePlate = request.licensePlate,
            status = request.status,
            shift = request.shift
        )
        return unitRepository.createUnit(unit)
    }

    suspend fun updateUnit(id: Int, request: UpdateUnitRequest): Unit? {
        val existingUnit = unitRepository.getUnitById(id) ?: return null
        val updatedUnit = existingUnit.copy(
            unitNumber = request.unitNumber,
            driverName = request.driverName,
            licensePlate = request.licensePlate,
            status = request.status,
            shift = request.shift
        )
        return unitRepository.updateUnit(id, updatedUnit)
    }

    suspend fun deleteUnit(id: Int): Boolean = unitRepository.deleteUnit(id)
}