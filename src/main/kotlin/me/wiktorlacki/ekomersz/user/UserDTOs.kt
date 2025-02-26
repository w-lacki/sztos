package me.wiktorlacki.ekomersz.user

data class UserResponse(val email: String, val username: String)

fun User.toResponse() = UserResponse(email, username)