package me.wiktorlacki.stos.user

import jakarta.persistence.*
import me.wiktorlacki.stos.auth.RefreshToken
import me.wiktorlacki.stos.contest.Contest
import me.wiktorlacki.stos.role.Role
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

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val refreshTokens: MutableList<RefreshToken> = mutableListOf(),

    @ManyToMany(mappedBy = "users", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val contests: MutableList<Contest> = mutableListOf(),

    @OneToMany(mappedBy = "creator", cascade = [CascadeType.ALL], orphanRemoval = true)
    val createdContests: MutableList<Contest> = mutableListOf(),

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
) {

    fun hasRole(name: String) = roles.any { it.name.equals("ROLE_${name}", true) }
}