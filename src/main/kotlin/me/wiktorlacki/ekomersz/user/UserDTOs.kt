package me.wiktorlacki.ekomersz.user

data class UserDTO(val email: String, val username: String)

fun User.toDTO() = UserDTO(email, username)