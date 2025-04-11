package me.wiktorlacki.stos.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableMethodSecurity
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity) = http
        .csrf { it.disable() }
        .cors {
            val configuration = CorsConfiguration()
            configuration.allowedOrigins = mutableListOf("*")
            configuration.allowedMethods = mutableListOf("*")
            configuration.allowedHeaders = mutableListOf("*")
            val source: UrlBasedCorsConfigurationSource = UrlBasedCorsConfigurationSource()
            source.registerCorsConfiguration("/**", configuration)
            it.configurationSource(source)
        }
        .authorizeHttpRequests {
            it.requestMatchers("/api/v1/auth/login").permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .oauth2ResourceServer {
            it.jwt(Customizer.withDefaults())
                .authenticationEntryPoint(BearerTokenAuthenticationEntryPoint())
                .accessDeniedHandler(BearerTokenAccessDeniedHandler())
        }
        .build()

    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration) = configuration.authenticationManager

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}