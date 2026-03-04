package com.motosurimbo.api.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object DatabaseSingleton {

    private lateinit var dataSource: HikariDataSource

    fun init() {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = "jdbc:postgresql://18.207.235.148:5434/motosurimbo_db"
            username = "postgres"
            password = "albafica"
            maximumPoolSize = 10
            isAutoCommit = true
        }

        dataSource = HikariDataSource(config)
        println("Conectado a PostgreSQL correctamente")
    }

    fun getConnection(): Connection = dataSource.connection
}