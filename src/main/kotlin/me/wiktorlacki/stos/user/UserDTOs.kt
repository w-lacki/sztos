package me.wiktorlacki.stos.user

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val username: String,
    val roles: List<String>
)

fun User.toResponse() = UserResponse(
    id!!,
    email,
    username,
    roles.map { it.name }
)