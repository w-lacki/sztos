package me.wiktorlacki.ekomersz.user

data class RegistrationRequestDTO(val username: String, val email: String, val password: String)

fun RegistrationRequestDTO.toEntity() = User(
    username = username,
    email = email,
    password = password
)

data class RegistrationResponseDTO(val username: String, val email: String)

fun User.toRegistrationResponseDTO() = RegistrationResponseDTO(this.username, this.email)

