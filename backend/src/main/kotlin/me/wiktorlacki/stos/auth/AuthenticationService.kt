package me.wiktorlacki.stos.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserRepository
import me.wiktorlacki.stos.role.RoleRepository
import me.wiktorlacki.stos.validateNotNull
import me.wiktorlacki.stos.validate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*


@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val emailVerificationService: EmailVerificationService,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager
) {

    @Transactional
    fun authenticate(request: LoginRequest): LoginResponse {
        val authToken = UsernamePasswordAuthenticationToken.unauthenticated(request.username, request.password)
        authenticationManager.authenticate(authToken)
        val user = userRepository.findByUsername(request.username) ?: throw UsernameNotFoundException(request.username)
        val accessToken = jwtService.generateToken(request.username, user.roles.map { it.name })

        val refreshToken = RefreshToken(
            user = user,
            expiresAt = Instant.now() + Duration.ofMinutes(5),
        )
        refreshTokenRepository.save(refreshToken)
        return JwtToken(accessToken).toLoginResponse(refreshToken.id!!)
    }

    fun refreshToken(refreshToken: UUID): LoginResponse {
        val refreshTokenEntity = refreshTokenRepository.findByIdAndExpiresAtAfter(refreshToken, Instant.now())

        validateNotNull(refreshTokenEntity) { "Invalid or expired refresh token." }
        val user = refreshTokenEntity!!.user
        val newAccessToken = jwtService.generateToken(user.username, user.roles.map { it.name })

        return JwtToken(newAccessToken).toLoginResponse(refreshToken)
    }

    fun revokeRefreshToken(refreshToken: UUID) {
        refreshTokenRepository.deleteById(refreshToken)
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
            }.toMutableSet(),
            emailVerified = true
        )

        userRepository.save(user)
        emailVerificationService.sendVerificationEmail(user)

        return user.toRegistrationResponse()
    }
}