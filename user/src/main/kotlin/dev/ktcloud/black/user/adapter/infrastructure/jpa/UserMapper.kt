package dev.ktcloud.black.user.adapter.infrastructure.jpa

import dev.ktcloud.black.common.adapter.infrastructure.jpa.EntityMapper
import dev.ktcloud.black.user.adapter.infrastructure.jpa.entity.User
import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import org.springframework.stereotype.Component

@Component
class UserMapper: EntityMapper<User, UserDomainEntity> {
    override fun toOrmEntity(domainEntity: UserDomainEntity): User {
        return User(
            id = domainEntity.id,
            role = domainEntity.role,
            email = domainEntity.email,
            password = domainEntity.password,
            name = domainEntity.name
        )
    }

    override fun toDomainEntity(entity: User): UserDomainEntity {
        return UserDomainEntity(
            id = entity.id,
            role = entity.role,
            email = entity.email,
            password = entity.password,
            name = entity.name
        )
    }
}