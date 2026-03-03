package com.motosurimbo.api.data.repositories

import com.motosurimbo.api.domain.models.Unit
import com.motosurimbo.api.domain.repositories.UnitRepository
import java.sql.Connection
import java.sql.ResultSet

class UnitRepositoryImpl : UnitRepository {

    override suspend fun getAllUnits(): List<Unit> {
        val units = mutableListOf<Unit>()
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            connection = com.motosurimbo.api.config.DatabaseSingleton.getConnection()

            statement = connection.prepareStatement(
                """
                SELECT u.id, u.unit_number, u.driver_name, u.license_plate,
                       COALESCE(us.name, 'Sin estado') as status_name,
                       COALESCE(s.name, 'Sin turno') as shift_name
                FROM units u
                LEFT JOIN unit_status us ON u.status_id = us.id
                LEFT JOIN shifts s ON u.shift_id = s.id
                ORDER BY u.id
                """
            )

            rs = statement.executeQuery()

            while (rs.next()) {
                units.add(
                    Unit(
                        id = rs.getInt("id"),
                        unitNumber = rs.getString("unit_number"),
                        driverName = rs.getString("driver_name"),
                        licensePlate = rs.getString("license_plate"),
                        status = rs.getString("status_name"),
                        shift = rs.getString("shift_name")
                    )
                )
            }
        } catch (e: Exception) {
            println("Error en getAllUnits: ${e.message}")
        } finally {
            rs?.close()
            statement?.close()
            connection?.close()
        }

        return units
    }

    override suspend fun getUnitById(id: Int): Unit? {
        var unit: Unit? = null
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            connection = com.motosurimbo.api.config.DatabaseSingleton.getConnection()

            statement = connection.prepareStatement(
                """
                SELECT u.id, u.unit_number, u.driver_name, u.license_plate,
                       COALESCE(us.name, 'Sin estado') as status_name,
                       COALESCE(s.name, 'Sin turno') as shift_name
                FROM units u
                LEFT JOIN unit_status us ON u.status_id = us.id
                LEFT JOIN shifts s ON u.shift_id = s.id
                WHERE u.id = ?
                """
            )
            statement.setInt(1, id)

            rs = statement.executeQuery()

            if (rs.next()) {
                unit = Unit(
                    id = rs.getInt("id"),
                    unitNumber = rs.getString("unit_number"),
                    driverName = rs.getString("driver_name"),
                    licensePlate = rs.getString("license_plate"),
                    status = rs.getString("status_name"),
                    shift = rs.getString("shift_name")
                )
            }
        } catch (e: Exception) {
            println("Error en getUnitById: ${e.message}")
        } finally {
            rs?.close()
            statement?.close()
            connection?.close()
        }

        return unit
    }

    override suspend fun createUnit(unit: Unit): Unit {
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null
        var rs: ResultSet? = null

        return try {
            connection = com.motosurimbo.api.config.DatabaseSingleton.getConnection()

            val statusId = getStatusId(connection, unit.status)
            val shiftId = getShiftId(connection, unit.shift)

            statement = connection.prepareStatement(
                "INSERT INTO units (unit_number, driver_name, license_plate, status_id, shift_id) VALUES (?, ?, ?, ?, ?) RETURNING id"
            )
            statement.setString(1, unit.unitNumber)
            statement.setString(2, unit.driverName)
            statement.setString(3, unit.licensePlate)
            statement.setInt(4, statusId)
            statement.setInt(5, shiftId)

            rs = statement.executeQuery()
            rs.next()
            val newId = rs.getInt("id")

            connection.commit()

            unit.copy(id = newId)
        } catch (e: Exception) {
            connection?.rollback()
            throw e
        } finally {
            rs?.close()
            statement?.close()
            connection?.close()
        }
    }

    override suspend fun updateUnit(id: Int, unit: Unit): Unit? {
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null

        return try {
            connection = com.motosurimbo.api.config.DatabaseSingleton.getConnection()

            val statusId = getStatusId(connection, unit.status)
            val shiftId = getShiftId(connection, unit.shift)

            statement = connection.prepareStatement(
                "UPDATE units SET unit_number = ?, driver_name = ?, license_plate = ?, status_id = ?, shift_id = ? WHERE id = ?"
            )
            statement.setString(1, unit.unitNumber)
            statement.setString(2, unit.driverName)
            statement.setString(3, unit.licensePlate)
            statement.setInt(4, statusId)
            statement.setInt(5, shiftId)
            statement.setInt(6, id)

            val updated = statement.executeUpdate()
            connection.commit()

            if (updated > 0) unit.copy(id = id) else null
        } catch (e: Exception) {
            connection?.rollback()
            throw e
        } finally {
            statement?.close()
            connection?.close()
        }
    }

    override suspend fun deleteUnit(id: Int): Boolean {
        var connection: Connection? = null
        var statement: java.sql.PreparedStatement? = null

        return try {
            connection = com.motosurimbo.api.config.DatabaseSingleton.getConnection()

            statement = connection.prepareStatement("DELETE FROM units WHERE id = ?")
            statement.setInt(1, id)

            val deletedRows = statement.executeUpdate()
            connection.commit()

            deletedRows > 0
        } catch (e: Exception) {
            connection?.rollback()
            throw e
        } finally {
            statement?.close()
            connection?.close()
        }
    }

    private fun getStatusId(connection: Connection, statusName: String): Int {
        var statement: java.sql.PreparedStatement? = null
        var rs: ResultSet? = null

        return try {
            statement = connection.prepareStatement("SELECT id FROM unit_status WHERE name = ?")
            statement.setString(1, statusName)
            rs = statement.executeQuery()
            rs.next()
            rs.getInt("id")
        } finally {
            rs?.close()
            statement?.close()
        }
    }

    private fun getShiftId(connection: Connection, shiftName: String): Int {
        var statement: java.sql.PreparedStatement? = null
        var rs: ResultSet? = null

        return try {
            statement = connection.prepareStatement("SELECT id FROM shifts WHERE name = ?")
            statement.setString(1, shiftName)
            rs = statement.executeQuery()
            rs.next()
            rs.getInt("id")
        } finally {
            rs?.close()
            statement?.close()
        }
    }
}