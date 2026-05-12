package dev.ktcloud.black.user.application.dto

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import dev.ktcloud.black.user.domain.vo.UserRole
import java.io.Serializable
import java.util.UUID

@JsonDeserialize
data class UserDto(
    val id: UUID,
    val role: UserRole,
    val email: String,
    val name: String,
): Serializable {
    companion object {
        fun from(user: UserDomainEntity): UserDto {
            return UserDto(
                id = user.id,
                role = user.role,
                email = user.email,
                name = user.name,
            )
        }
    }
}
