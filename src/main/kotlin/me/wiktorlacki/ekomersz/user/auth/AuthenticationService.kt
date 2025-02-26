package me.wiktorlacki.ekomersz.user.auth

import me.wiktorlacki.ekomersz.jwt.JwtService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    fun authenticate(request: AuthenticationRequestDTO): AuthenticationResponseDTO {
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)
        authenticationManager.authenticate(authToken)
        return AuthenticationResponseDTO(jwtService.generateToken(request.username));
    }
}