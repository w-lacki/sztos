package me.wiktorlacki.stos.auth

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import java.time.Duration
import java.time.Instant

class JwtService(private val issuer: String, private val ttl: Duration, private val encoder: JwtEncoder) {

    fun generateToken(username: String, roles: List<String>): String {
        val claims = JwtClaimsSet.builder()
            .subject(username)
            .claim("roles", roles.joinToString(prefix = "[", postfix = "]", separator = ", "))
            .issuer(issuer)
            .expiresAt(Instant.now() + ttl)
            .build()
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}