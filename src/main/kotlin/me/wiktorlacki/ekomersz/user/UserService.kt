package me.wiktorlacki.ekomersz.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getById(id: UUID) = userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    fun getByUsername(username: String) = userRepository.findByUsername(username) ?: throw UserNotFoundException()
    fun getAllByUsernameContaining(username: String) = userRepository.findByUsernameContainingIgnoreCase(username)
}