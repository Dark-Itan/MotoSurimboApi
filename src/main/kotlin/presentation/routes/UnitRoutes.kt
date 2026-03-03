package com.motosurimbo.api.presentation.routes

import com.motosurimbo.api.domain.models.CreateUnitRequest
import com.motosurimbo.api.domain.models.UpdateUnitRequest
import com.motosurimbo.api.domain.services.UnitService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.unitRouting(unitService: UnitService) {
    route("/units") {
        get {
            val units = unitService.getAllUnits()
            call.respond(HttpStatusCode.OK, units)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val unit = unitService.getUnitById(id)
            if (unit == null) {
                call.respond(HttpStatusCode.NotFound, "Unidad no encontrada")
            } else {
                call.respond(HttpStatusCode.OK, unit)
            }
        }

        post {
            try {
                val request = call.receive<CreateUnitRequest>()
                val newUnit = unitService.createUnit(request)
                call.respond(HttpStatusCode.Created, newUnit)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos: ${e.message}")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@put
            }

            try {
                val request = call.receive<UpdateUnitRequest>()
                val updatedUnit = unitService.updateUnit(id, request)

                if (updatedUnit == null) {
                    call.respond(HttpStatusCode.NotFound, "Unidad no encontrada")
                } else {
                    call.respond(HttpStatusCode.OK, updatedUnit)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Datos inválidos: ${e.message}")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@delete
            }

            val deleted = unitService.deleteUnit(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Unidad eliminada")
            } else {
                call.respond(HttpStatusCode.NotFound, "Unidad no encontrada")
            }
        }
    }
}