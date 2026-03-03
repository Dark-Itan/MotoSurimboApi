package com.motosurimbo

import io.ktor.server.application.*
import com.motosurimbo.api.config.DatabaseSingleton
import com.motosurimbo.api.data.repositories.UserRepositoryImpl
import com.motosurimbo.api.data.repositories.UnitRepositoryImpl
import com.motosurimbo.api.domain.services.AuthService
import com.motosurimbo.api.domain.services.UnitService

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()

    configureHTTP()
    configureMonitoring()
    configureSerialization()

    val userRepository = UserRepositoryImpl()
    val authService = AuthService(userRepository)

    val unitRepository = UnitRepositoryImpl()
    val unitService = UnitService(unitRepository)

    configureRouting(authService, unitService)
}