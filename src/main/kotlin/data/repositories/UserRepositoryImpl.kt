package com.motosurimbo.api.data.repositories

import com.motosurimbo.api.domain.models.User
import com.motosurimbo.api.domain.repositories.UserRepository
import com.motosurimbo.api.config.DatabaseSingleton
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class UserRepositoryImpl : UserRepository {

    override suspend fun findByUsername(username: String): User? {
        var user: User? = null
        var connection: Connection? = null
        var statement: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            // POOL DE HIKARCP
            connection = DatabaseSingleton.getConnection()

            statement = connection.prepareStatement(
                "SELECT id, username, password_hash, full_name FROM users WHERE username = ?"
            )
            statement.setString(1, username)

            rs = statement.executeQuery()

            if (rs.next()) {
                user = User(
                    id = rs.getInt("id"),
                    username = rs.getString("username"),
                    passwordHash = rs.getString("password_hash"),
                    fullName = rs.getString("full_name")
                )
            }
        } catch (e: SQLException) {
            println("Error en consulta: ${e.message}")
            throw e
        } finally {
            rs?.close()
            statement?.close()
            connection?.close()  // ← ESTO DEVUELVE LA CONEXIÓN AL POOL
        }

        return user
    }
}