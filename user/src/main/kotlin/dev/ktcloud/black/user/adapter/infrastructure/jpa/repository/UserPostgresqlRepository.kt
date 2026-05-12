package dev.ktcloud.black.user.adapter.infrastructure.jpa.repository

import dev.ktcloud.black.user.adapter.infrastructure.jpa.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserPostgresqlRepository: CrudRepository<User, UUID> {
    fun findByEmail(email: String): User?
}