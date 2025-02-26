package me.wiktorlacki.ekomersz.configuration

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import me.wiktorlacki.ekomersz.user.auth.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration

@Configuration
class JwtConfiguration {

    @Value("\${security.jwt.private_key}")
    private lateinit var privateKey: RSAPrivateKey

    @Value("\${security.jwt.public_key}")
    private lateinit var publicKey: RSAPublicKey

    @Value("\${security.jwt.ttl}")
    private lateinit var ttl: Duration

    @Bean
    fun jwtEncoder() = NimbusJwtEncoder(
        ImmutableJWKSet(
            JWKSet(
                RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .build()
            )
        )
    )

    @Bean
    fun jwtDecoder() = NimbusJwtDecoder.withPublicKey(publicKey).build()

    @Bean
    fun jwtService(@Value("\${spring.application.name}") appName: String, jwtEncoder: JwtEncoder) =
        JwtService(appName, ttl, jwtEncoder)
}