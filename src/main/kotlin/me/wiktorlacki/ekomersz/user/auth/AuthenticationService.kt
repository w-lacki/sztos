package me.wiktorlacki.ekomersz.user.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.ekomersz.user.User
import me.wiktorlacki.ekomersz.user.UserRepository
import me.wiktorlacki.ekomersz.user.role.RoleRepository
import me.wiktorlacki.ekomersz.validate
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    fun authenticate(request: LoginRequest): LoginResponse {
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)
        authenticationManager.authenticate(authToken)
        val token = jwtService.generateToken(request.username)
        return JwtToken(token).toLoginResponse()
    }

    @Transactional
    fun register(request: RegistrationRequest): RegistrationResponse {
        validate(userRepository.existsByEmail(request.email).not()) { "User with this email already exists" }
        validate(userRepository.existsByUsername(request.username).not()) { "User with this username already exists" }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            roles = buildSet {
                roleRepository.findByName("ROLE_USER")?.let { add(it) }
            }.toMutableSet()
        )

        return userRepository.save(user).toRegistrationResponse()
    }
}