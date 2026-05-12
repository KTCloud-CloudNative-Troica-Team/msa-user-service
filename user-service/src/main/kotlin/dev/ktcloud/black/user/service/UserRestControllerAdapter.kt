package dev.ktcloud.black.user.service

import dev.ktcloud.black.user.adapter.presentation.web.inbound.UserRestController
import dev.ktcloud.black.user.adapter.presentation.web.inbound.request.CreateUserRequest
import dev.ktcloud.black.user.adapter.presentation.web.inbound.request.FetchMeRequest
import dev.ktcloud.black.user.adapter.presentation.web.inbound.response.CreateUserResponse
import dev.ktcloud.black.user.adapter.presentation.web.inbound.response.FetchMeResponse
import dev.ktcloud.black.user.application.dto.UserDto
import dev.ktcloud.black.user.application.port.inbound.CreateUserCommand
import dev.ktcloud.black.user.application.port.inbound.FetchMeQuery
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * `user/` 라이브러리의 `UserRestController` 인터페이스 구현 (모노레포에는 인터페이스만 정의됨).
 *
 * 경로 prefix `/api/v1/users` — api-gateway의 JwtHeaderCheckFilter 보호 영역.
 *
 * 회원가입(SignUp + JWT 발급) 정식 흐름은 api-gateway → auth-service.
 * 본 컨트롤러의 `createUser`는 user 도메인만 생성하는 관리용 endpoint
 * (auth-service의 SignUp 내부 호출 경로와 별도).
 */
@RestController
@RequestMapping("/api/v1/users")
class UserRestControllerAdapter(
    private val createUserCommand: CreateUserCommand,
    private val fetchMeQuery: FetchMeQuery,
) : UserRestController {

    @PostMapping
    override fun createUser(@RequestBody req: CreateUserRequest): CreateUserResponse {
        val out = createUserCommand.create(
            CreateUserCommand.In(
                email = req.email,
                plainPassword = req.plainPassword,
                name = req.name,
            )
        )
        return CreateUserResponse(
            profile = UserDto(
                id = out.id,
                role = out.role,
                email = out.email,
                name = out.name,
            )
        )
    }

    @PostMapping("/me")
    override fun fetchMe(@RequestBody req: FetchMeRequest): FetchMeResponse {
        val out = fetchMeQuery.fetchMe(FetchMeQuery.In(UUID.fromString(req.id)))
        return FetchMeResponse(
            profile = UserDto(
                id = out.id,
                role = out.role,
                email = out.email,
                name = out.name,
            )
        )
    }
}
