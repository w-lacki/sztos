package me.wiktorlacki.ekomersz.user.auth

import me.wiktorlacki.ekomersz.user.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class JpaUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user =
            userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User $username not found")
        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }

        return User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(authorities)
            .build()
    }

}