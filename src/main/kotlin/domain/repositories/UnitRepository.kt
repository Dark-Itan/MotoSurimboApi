package com.motosurimbo.api.domain.repositories

import com.motosurimbo.api.domain.models.Unit

interface UnitRepository {
    suspend fun getAllUnits(): List<Unit>
    suspend fun getUnitById(id: Int): Unit?
    suspend fun createUnit(unit: Unit): Unit
    suspend fun updateUnit(id: Int, unit: Unit): Unit?
    suspend fun deleteUnit(id: Int): Boolean
}