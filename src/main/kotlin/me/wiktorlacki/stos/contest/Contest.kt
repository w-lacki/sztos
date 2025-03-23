package me.wiktorlacki.stos.contest

import jakarta.persistence.*
import me.wiktorlacki.stos.problem.Problem
import me.wiktorlacki.stos.user.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "contests")
@EntityListeners(AuditingEntityListener::class)
class Contest(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column
    val title: String,

    @Column
    val description: String,

    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    val creator: User,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_contests",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "contest_id", referencedColumnName = "id")]
    )
    var users: MutableList<User> = mutableListOf(),

    @OneToMany(mappedBy = "contest", cascade = [CascadeType.ALL], orphanRemoval = true)
    var problems: MutableList<Problem> = mutableListOf(),

    @Column(
        name = "created_at", nullable = false, updatable = false
    )
    @CreatedDate
    var createdAt: Instant? = null,

    @Column(
        name = "updated_at", nullable = false, updatable = false
    )
    @LastModifiedDate
    private var updatedAt: Instant? = null
)