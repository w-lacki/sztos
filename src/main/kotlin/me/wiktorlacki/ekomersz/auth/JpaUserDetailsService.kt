package me.wiktorlacki.ekomersz.auth

import me.wiktorlacki.ekomersz.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import kotlin.math.log

@Service
class JpaUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {


    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User $username not found")

        if (!user.emailVerified) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User not verified.")

        val logger = LoggerFactory.getLogger(this.javaClass)
        logger.info("ESSA :)")
        LoggerFactory.getLogger(this.javaClass).info("Loading user ${user.username} with authorities ${user.roles}")
        return User.builder()
            .username(username)
            .password(user.password)
            .authorities(user.roles.map { SimpleGrantedAuthority(it.name) })
            .build()
    }

}