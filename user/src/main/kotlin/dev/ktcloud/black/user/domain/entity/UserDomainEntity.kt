package dev.ktcloud.black.user.domain.entity

import dev.ktcloud.black.user.domain.vo.UserRole
import java.util.UUID

data class UserDomainEntity(
    val id: UUID = UUID.randomUUID(),
    val role: UserRole,
    val email: String,
    val password: String,
    val name: String,
)