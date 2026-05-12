package dev.ktcloud.black.user.adapter.presentation.web.inbound

import dev.ktcloud.black.user.adapter.presentation.web.inbound.request.CreateUserRequest
import dev.ktcloud.black.user.adapter.presentation.web.inbound.request.FetchMeRequest
import dev.ktcloud.black.user.adapter.presentation.web.inbound.response.CreateUserResponse
import dev.ktcloud.black.user.adapter.presentation.web.inbound.response.FetchMeResponse

interface UserRestController {
    fun createUser(req: CreateUserRequest): CreateUserResponse
    fun fetchMe(req: FetchMeRequest): FetchMeResponse
}