package dev.ktcloud.black.user.adapter.presentation.web.inbound.response

import dev.ktcloud.black.user.application.dto.UserDto

data class FetchMeResponse(
    val profile: UserDto
)
