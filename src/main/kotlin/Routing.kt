package com.motosurimbo

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import com.motosurimbo.api.domain.services.AuthService
import com.motosurimbo.api.domain.services.UnitService
import com.motosurimbo.api.presentation.routes.authRouting
import com.motosurimbo.api.presentation.routes.unitRouting

fun Application.configureRouting(
    authService: AuthService,
    unitService: UnitService
) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(
                text = "Error interno: ${cause.message}",
                status = HttpStatusCode.InternalServerError
            )
        }
    }

    routing {
        get("/") {
            call.respondText("MotoSurimbo API funcionando 🏍️")
        }

        authRouting(authService)
        unitRouting(unitService)
    }
}