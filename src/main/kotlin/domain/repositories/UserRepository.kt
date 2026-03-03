package com.motosurimbo.api.domain.repositories

import com.motosurimbo.api.domain.models.User

interface UserRepository {
    suspend fun findByUsername(username: String): User?
}