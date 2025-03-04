package me.wiktorlacki.ekomersz.user

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByUsername(username: String): User?
    fun findByUsernameContainingIgnoreCase(username: String): List<User>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}