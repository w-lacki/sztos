package me.wiktorlacki.ekomersz.user.auth

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserRegistrationController(
    private val userRegistrationService: UserRegistrationService,
) {


    @PostMapping("/register")
    fun register(
        @RequestBody @Valid @Validated request: RegistrationRequest,
        result: BindingResult
    ): ResponseEntity<RegistrationResponse> {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build()
        }
        val registeredUser = userRegistrationService.register(request)
        return ResponseEntity.ok(registeredUser.toRegistrationResponseDTO())
    }
}