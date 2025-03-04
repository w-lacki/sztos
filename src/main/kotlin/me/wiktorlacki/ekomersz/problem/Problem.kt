package me.wiktorlacki.ekomersz.problem

import jakarta.persistence.*
import me.wiktorlacki.ekomersz.contest.Contest
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "problems")
@EntityListeners(AuditingEntityListener::class)
class Problem(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column
    var title: String,

    @Column
    var description: String,

    @ManyToOne(optional = false)
    @JoinColumn(name = "contest_id")
    val contest: Contest,

    @Column
    var deadline: Instant,

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