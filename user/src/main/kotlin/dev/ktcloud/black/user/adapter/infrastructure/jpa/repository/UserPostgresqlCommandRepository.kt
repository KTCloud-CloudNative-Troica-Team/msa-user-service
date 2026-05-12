package dev.ktcloud.black.user.adapter.infrastructure.jpa.repository

import dev.ktcloud.black.user.adapter.infrastructure.jpa.UserMapper
import dev.ktcloud.black.user.application.port.outbound.UserCommandOutboundPort
import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import org.springframework.stereotype.Component

@Component
class UserPostgresqlCommandRepository(
    private val repository: UserPostgresqlRepository,
    private val mapper: UserMapper
): UserCommandOutboundPort {
    override fun save(user: UserDomainEntity): UserDomainEntity {
        val saved = repository.save(
            mapper.toOrmEntity(user)
        )

        return mapper.toDomainEntity(saved)
    }
}