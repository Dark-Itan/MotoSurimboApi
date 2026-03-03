package com.motosurimbo.api.domain.services

import com.motosurimbo.api.domain.models.LoginRequest
import com.motosurimbo.api.domain.models.LoginResponse
import com.motosurimbo.api.domain.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt

class AuthService(
    private val userRepository: UserRepository
) {
    suspend fun login(request: LoginRequest): LoginResponse {
        // 1. Buscar usuario por username
        val user = userRepository.findByUsername(request.username)

        // 2. Validar si existe y la contraseña es correcta
        return if (user != null && BCrypt.checkpw(request.password, user.passwordHash)) {
            LoginResponse(
                success = true,
                message = "Login exitoso",
                token = "temp-token-${user.id}"
            )
        } else {
            LoginResponse(
                success = false,
                message = "Usuario o contraseña incorrectos"
            )
        }
    }
}