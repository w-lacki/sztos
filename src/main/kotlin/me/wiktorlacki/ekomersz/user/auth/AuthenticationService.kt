package me.wiktorlacki.ekomersz.user.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.ekomersz.user.User
import me.wiktorlacki.ekomersz.user.UserRepository
import me.wiktorlacki.ekomersz.user.role.RoleRepository
import me.wiktorlacki.ekomersz.validateNotNull
import me.wiktorlacki.ekomersz.validate
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
        val accessToken = jwtService.generateToken(request.username)
        val user = userRepository.findByUsername(request.username) ?: throw UsernameNotFoundException(request.username)

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
        val newAccessToken = jwtService
            .generateToken(refreshTokenEntity!!.user.username)

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
            }.toMutableSet()
        )

        userRepository.save(user)
        emailVerificationService.sendVerificationEmail(user)

        return user.toRegistrationResponse()
    }
}