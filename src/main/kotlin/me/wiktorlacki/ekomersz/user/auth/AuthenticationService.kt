package me.wiktorlacki.ekomersz.user.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.ekomersz.user.User
import me.wiktorlacki.ekomersz.user.UserRepository
import me.wiktorlacki.ekomersz.validate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtService: JwtService
) {

    fun authenticate(request: LoginRequest): LoginResponse {
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)
        require(authenticationManager.authenticate(authToken).isAuthenticated) { "essa!!" }
        return LoginResponse(jwtService.generateToken(request.username));
    }


    @Transactional
    fun register(request: RegistrationRequest): User {
        validate(userRepository.existsByEmail(request.email).not()) { "User with this email already exists" }
        validate(userRepository.existsByUsername(request.username).not()) { "User with this username already exists" }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )

        return userRepository.save(user)
    }
}