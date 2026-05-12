package dev.ktcloud.black.user.application.service

import dev.ktcloud.black.user.adapter.infrastructure.jpa.repository.UserPostgresqlCommandRepository
import dev.ktcloud.black.user.application.port.inbound.CreateUserCommand
import dev.ktcloud.black.user.domain.entity.UserDomainEntity
import dev.ktcloud.black.user.domain.vo.UserRole
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserCommandService(
    private val userPostgresqlCommandRepository: UserPostgresqlCommandRepository,
    private val passwordEncoder: PasswordEncoder,
): CreateUserCommand {
    @Transactional
    override fun create(command: CreateUserCommand.In): CreateUserCommand.Out {
        val userDomainEntity = UserDomainEntity(
            role = UserRole.USER,
            email = command.email,
            password = passwordEncoder.encode(command.plainPassword),
            name = command.name,
        )

        val saved = userPostgresqlCommandRepository.save(userDomainEntity)

        return CreateUserCommand.Out.from(saved)
    }
}