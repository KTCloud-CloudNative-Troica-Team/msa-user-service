package dev.ktcloud.black.user.adapter.infrastructure.jpa.repository

import dev.ktcloud.black.user.adapter.infrastructure.jpa.UserMapper
import dev.ktcloud.black.user.application.port.outbound.UserQueryOutboundPort
import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import dev.ktcloud.black.user.domain.exception.UserException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserPostgresqlQueryRepository(
    private val repository: UserPostgresqlRepository,
    private val mapper: UserMapper
): UserQueryOutboundPort {
    override fun findById(id: UUID): UserDomainEntity {
        val ormEntity = repository.findById(id)

        if (!ormEntity.isPresent)
            throw UserException.UserNotFoundException()

        return mapper.toDomainEntity(ormEntity.get())
    }

    override fun findByEmail(email: String): UserDomainEntity {
        val ormEntity = repository.findByEmail(email) ?: throw UserException.UserNotFoundException()

        return mapper.toDomainEntity(ormEntity)
    }
}