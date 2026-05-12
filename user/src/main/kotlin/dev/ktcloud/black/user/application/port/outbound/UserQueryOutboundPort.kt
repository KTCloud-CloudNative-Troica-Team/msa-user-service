package dev.ktcloud.black.user.application.port.outbound

import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import java.util.UUID

interface UserQueryOutboundPort {
    fun findById(id: UUID): UserDomainEntity
    fun findByEmail(email: String): UserDomainEntity
}