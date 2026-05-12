package dev.ktcloud.black.user.domain.exception

import dev.ktcloud.black.common.exception.CustomException
import org.springframework.http.HttpStatus

sealed class UserException {
    class UserNotFoundException(message: String? = null, e: Throwable? = null) : CustomException("001", message ?: "ユーザーが見つかりません", HttpStatus.NOT_FOUND, e)
}