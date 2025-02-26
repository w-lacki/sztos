package me.wiktorlacki.ekomersz.user

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private var id: UUID? = null,

    @Column(nullable = false, unique = true, updatable = false)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @NotNull
    @NotBlank
    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var emailVerified: Boolean = false,

    @Column(
        name = "created_at", nullable = false, updatable = false
    )
    @CreatedDate
    private var createdAt: Instant? = null,

    @Column(
        name = "updated_at", nullable = false, updatable = false
    )
    @LastModifiedDate
    private var updatedAt: Instant? = null
)