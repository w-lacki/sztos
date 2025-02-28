package me.wiktorlacki.ekomersz.user.auth

import jakarta.persistence.*
import me.wiktorlacki.ekomersz.user.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity(name = "refresh_tokens")
@EntityListeners(AuditingEntityListener::class)
class RefreshToken(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    val user: User,

    @Column(name = "expires_at", nullable = false, updatable = false)
    var expiresAt: Instant?,

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private var createdAt: Instant? = null

)