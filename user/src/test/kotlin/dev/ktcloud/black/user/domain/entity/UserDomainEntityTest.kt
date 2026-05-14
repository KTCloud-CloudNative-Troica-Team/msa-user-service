package dev.ktcloud.black.user.domain.entity

import dev.ktcloud.black.user.domain.vo.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.UUID

/**
 * R-57 (평가 기본 (3)-1): UserDomainEntity 단위 테스트.
 *
 * 회원 도메인 entity 의 핵심 동작 — UUID 자동 생성 + role/email/name 보존 + data
 * class equality / copy.
 */
@DisplayName("UserDomainEntity - 회원 도메인 entity")
class UserDomainEntityTest {

    @Test
    @DisplayName("id 미지정 시 UUID 자동 생성")
    fun `id default 가 UUID randomUUID`() {
        val u = UserDomainEntity(
            role = UserRole.USER,
            email = "user@example.com",
            password = "encoded",
            name = "홍길동",
        )

        assertThat(u.id).isNotNull()
        assertThat(u.id.toString()).hasSize(36)
    }

    @Test
    @DisplayName("두 회원은 서로 다른 UUID 보유")
    fun `각 회원의 UUID 는 unique`() {
        val u1 = UserDomainEntity(role = UserRole.USER, email = "a@x.com", password = "p", name = "A")
        val u2 = UserDomainEntity(role = UserRole.USER, email = "b@x.com", password = "p", name = "B")

        assertThat(u1.id).isNotEqualTo(u2.id)
    }

    @Test
    @DisplayName("UserRole.ADMIN 으로 생성한 회원의 role 보존")
    fun `ADMIN role 보존`() {
        val admin = UserDomainEntity(
            role = UserRole.ADMIN,
            email = "admin@x.com",
            password = "p",
            name = "관리자",
        )

        assertThat(admin.role).isEqualTo(UserRole.ADMIN)
    }

    @Test
    @DisplayName("copy() 로 password 만 갱신 — 나머지 필드 보존")
    fun `copy 로 password 갱신`() {
        val original = UserDomainEntity(
            role = UserRole.USER,
            email = "u@x.com",
            password = "old_hash",
            name = "사용자",
        )
        val updated = original.copy(password = "new_hash")

        assertThat(updated.id).isEqualTo(original.id)
        assertThat(updated.role).isEqualTo(original.role)
        assertThat(updated.email).isEqualTo(original.email)
        assertThat(updated.name).isEqualTo(original.name)
        assertThat(updated.password).isEqualTo("new_hash")
    }

    @Test
    @DisplayName("동일 모든 필드의 두 회원은 data class equality 로 동등")
    fun `data class equality`() {
        val fixedId = UUID.randomUUID()
        val u1 = UserDomainEntity(id = fixedId, role = UserRole.USER, email = "e", password = "p", name = "N")
        val u2 = UserDomainEntity(id = fixedId, role = UserRole.USER, email = "e", password = "p", name = "N")

        assertThat(u1).isEqualTo(u2)
        assertThat(u1.hashCode()).isEqualTo(u2.hashCode())
    }
}
