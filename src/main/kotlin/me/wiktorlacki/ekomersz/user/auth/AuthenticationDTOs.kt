package me.wiktorlacki.ekomersz.user.auth

data class AuthenticationRequestDTO(val username: String, val password: String)

data class AuthenticationResponseDTO(val token: String)