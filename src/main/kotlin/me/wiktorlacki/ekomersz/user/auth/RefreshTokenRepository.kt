package me.wiktorlacki.ekomersz.user.auth

import org.springframework.data.repository.CrudRepository
import java.time.Instant
import java.util.UUID

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID> {

    fun findByIdAndExpiresAtAfter(id: UUID, date: Instant): RefreshToken?
}