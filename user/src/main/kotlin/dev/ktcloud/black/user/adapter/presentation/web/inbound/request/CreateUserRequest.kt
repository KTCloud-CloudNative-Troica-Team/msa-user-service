package dev.ktcloud.black.user.adapter.presentation.web.inbound.request

data class CreateUserRequest(
    val email: String,
    val plainPassword: String,
    val name: String
)
