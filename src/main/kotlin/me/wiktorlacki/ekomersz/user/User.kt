package me.wiktorlacki.ekomersz.user

import jakarta.persistence.*
import me.wiktorlacki.ekomersz.user.role.Role
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
    var id: UUID? = null,

    @Column(nullable = false, unique = true, updatable = false)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var emailVerified: Boolean = false,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roles: MutableSet<Role> = mutableSetOf(),

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