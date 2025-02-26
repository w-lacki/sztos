package me.wiktorlacki.ekomersz.user.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import me.wiktorlacki.ekomersz.user.User

data class LoginRequest(
    @get:NotBlank(message = "Username cannot be blank.")
    @get:Size(min = 3, max = 16, message = "Username must be between 3 and 16 characters.")
    val username: String,

    @get:Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters.")
    @get:NotBlank(message = "Password cannot be blank.")
    val password: String
)

data class LoginResponse(val token: String)
fun JwtToken.toLoginResponse() = LoginResponse(token)

data class RegistrationRequest(

    @get:NotBlank(message = "Username cannot be blank.")
    @get:Size(min = 3, max = 16, message = "Username must be between 3 and 16 characters.")
    @get:Pattern(
        regexp = "[a-zA-Z0-9_]+",
        message = "Username must consist of alphanumeric characters, numbers or underscores."
    )
    val username: String,
    @get:Email(message = "Invalid email address.")
    val email: String,

    @get:NotBlank(message = "Password cannot be blank.")
    @get:Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters.")
    val password: String
)

data class RegistrationResponse(val username: String, val email: String)
fun User.toRegistrationResponse() = RegistrationResponse(username, email)