package dev.ktcloud.black.user.domain.vo

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable
import java.util.UUID

data class UserDetail(
    val id: UUID,
    val role: UserRole,
    val email: String,
    val name: String,
) : UserDetails, Serializable {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf<GrantedAuthority>(
        SimpleGrantedAuthority(role.toString())
    )

    override fun getPassword(): String = ""

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}