package me.wiktorlacki.stos.auth

import me.wiktorlacki.stos.user.UserRepository
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

        if (!user.emailVerified) throw EmailNotVerifiedException()

        return User.builder()
            .username(username)
            .password(user.password)
            .authorities(user.roles.map { SimpleGrantedAuthority(it.name) })
            .build()
    }

}