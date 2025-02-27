package me.wiktorlacki.ekomersz.user

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(private val userRepository: UserRepository) {

    fun getById(id: UUID): User? = userRepository.findByIdOrNull(id)
    fun getByUsername(username: String): User? = userRepository.findByUsername(username)
}