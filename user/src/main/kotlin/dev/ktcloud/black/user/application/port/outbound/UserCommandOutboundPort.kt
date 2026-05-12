package dev.ktcloud.black.user.application.port.outbound

import dev.ktcloud.black.user.domain.entity.UserDomainEntity

interface UserCommandOutboundPort {
    fun save(user: UserDomainEntity): UserDomainEntity
}