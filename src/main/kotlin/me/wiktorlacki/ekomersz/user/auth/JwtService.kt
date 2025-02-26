package me.wiktorlacki.ekomersz.jwt

import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import java.time.Duration
import java.time.Instant

class JwtService(val issuer: String, val ttl: Duration, val encoder: JwtEncoder) {


    fun generateToken(username: String): String {
        val claims = JwtClaimsSet.builder()
            .subject(username)
            .issuer(issuer)
            .expiresAt(Instant.now() + ttl)
            .build()
        return encoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}