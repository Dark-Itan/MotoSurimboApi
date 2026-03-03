package com.motosurimbo.api.presentation.routes

import com.motosurimbo.api.domain.models.LoginRequest
import com.motosurimbo.api.domain.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRouting(authService: AuthService) {
    route("/auth") {
        post("/login") {
            try {
                // 1. Recibir JSON del cliente
                val request = call.receive<LoginRequest>()

                // 2. Llamar al servicio (capa domain)
                val response = authService.login(request)

                // 3. Responder según el resultado
                if (response.success) {
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, response)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Datos inválidos"))
            }
        }
    }
}