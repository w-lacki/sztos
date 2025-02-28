package me.wiktorlacki.ekomersz.user.auth

import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/auth")
class AuthenticationController(
    private val authenticationService: AuthenticationService,
    private val emailVerificationService: EmailVerificationService
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

    @GetMapping("/verify/{id}")
    fun verify(@PathVariable id: UUID, @RequestParam code: String): ResponseEntity<VerificationResponse> {
        val response = emailVerificationService.verifyEmail(id, code)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh-token")
    fun refreshToken(@RequestParam refreshToken: UUID): ResponseEntity<LoginResponse> {
        val response = authenticationService.refreshToken(refreshToken)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(@RequestParam refreshToken: UUID): ResponseEntity<Unit> {
        authenticationService.revokeRefreshToken(refreshToken)
        return ResponseEntity.accepted().build()
    }
}