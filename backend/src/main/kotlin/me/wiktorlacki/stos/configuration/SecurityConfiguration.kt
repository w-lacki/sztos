package me.wiktorlacki.stos.configuration

import org.springframework.boot.webservices.client.WebServiceMessageSenderFactory.http
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
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableMethodSecurity
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf {
                disable()
            }
            cors {
                val corsConfig = CorsConfiguration().apply {
                    allowedOrigins = listOf("http://localhost:3000")
                    allowedMethods = listOf("*")
                    allowedHeaders = listOf("*")
                }
                configurationSource = UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", corsConfig)
                }
            }
            authorizeHttpRequests {
                authorize("/api/v1/auth/*", permitAll)
                authorize(anyRequest, authenticated)
            }
            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }
            oauth2ResourceServer {
                jwt {
                    authenticationEntryPoint = BearerTokenAuthenticationEntryPoint()
                    accessDeniedHandler = BearerTokenAccessDeniedHandler()
                }
            }
        }
        return http.build()
    }


    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration) = configuration.authenticationManager

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}