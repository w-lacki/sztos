package me.wiktorlacki.ekomersz.user

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class RegistrationController(
    private val userRegistrationService: UserRegistrationService,
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegistrationRequestDTO): ResponseEntity<RegistrationResponseDTO> {
        val registeredUser = userRegistrationService.register(request)
        return ResponseEntity.ok(registeredUser.toRegistrationResponseDTO())
    }
}