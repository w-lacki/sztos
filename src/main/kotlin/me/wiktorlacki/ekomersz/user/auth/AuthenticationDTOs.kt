package me.wiktorlacki.ekomersz.user.auth

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import me.wiktorlacki.ekomersz.user.User
import java.util.UUID

data class LoginRequest(
    @get:NotBlank(message = "Username cannot be blank.")
    @get:Size(min = 3, max = 16, message = "Username must be between 3 and 16 characters.")
    val username: String,

    @get:Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters.")
    @get:NotBlank(message = "Password cannot be blank.")
    val password: String
)

data class LoginResponse(val token: String, val refreshToken: String)

fun JwtToken.toLoginResponse(refreshToken: UUID) = LoginResponse(token, refreshToken.toString())

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

data class RegistrationResponse(
    val username: String,
    val email: String,

    @JsonProperty("requires_confirmation")
    val requiresConfirmation: Boolean = true
)

fun User.toRegistrationResponse() = RegistrationResponse(
    username,
    email
)

data class VerificationResponse(
    val email: String, @JsonProperty("requires_confirmation")
    val requiresConfirmation: Boolean = false
)

fun User.toVerificationResponse() = VerificationResponse(email)
