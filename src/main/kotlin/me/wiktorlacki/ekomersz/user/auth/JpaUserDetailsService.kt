package me.wiktorlacki.ekomersz.user.auth

import me.wiktorlacki.ekomersz.user.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class JpaUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User $username not found")

        if (!user.emailVerified) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User not verified.")
        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }

        return User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(authorities)
            .build()
    }

}