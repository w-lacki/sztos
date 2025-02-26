package me.wiktorlacki.ekomersz.user

import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getByUsername(username: String): User? = userRepository.findByUsername(username)
}