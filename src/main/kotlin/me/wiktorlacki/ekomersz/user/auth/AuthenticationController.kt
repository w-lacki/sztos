package me.wiktorlacki.ekomersz.user.auth

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun login(@RequestBody @Valid request: LoginRequest): ResponseEntity<LoginResponse> {
        return ResponseEntity.ok(authenticationService.authenticate(request))
    }

    @PostMapping("/register")
    fun register(@RequestBody @Valid request: RegistrationRequest): ResponseEntity<RegistrationResponse> {
        val registeredUser = authenticationService.register(request)
        return ResponseEntity.ok(registeredUser.toRegistrationResponse())
    }
}