package me.wiktorlacki.ekomersz.user.auth

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.function.ServerResponse

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authenticationService.authenticate(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/register", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun register(@RequestBody @Valid request: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val response = authenticationService.register(request)
        return ResponseEntity.ok(response)
    }
}