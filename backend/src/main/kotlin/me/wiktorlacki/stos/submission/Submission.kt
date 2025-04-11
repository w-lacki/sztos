package me.wiktorlacki.stos.submission

import jakarta.persistence.*
import me.wiktorlacki.stos.problem.Problem
import me.wiktorlacki.stos.user.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant


@Entity
@Table(name = "submissions")
@EntityListeners(AuditingEntityListener::class)
class Submission(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    val submitter: User,

    @ManyToOne(optional = false)
    @JoinColumn(name = "problem_id")
    val problem: Problem,

    @Column
    var state: State,

    @Column(name = "source_code", nullable = false, updatable = false, columnDefinition = "TEXT")
    val sourceCode: String,

    @Column(
        name = "created_at", nullable = false, updatable = false
    )
    @CreatedDate
    private var createdAt: Instant? = null,

    @Column(name = "updated_at", nullable = false, updatable = false)
    @LastModifiedDate
    private var updatedAt: Instant? = null
) {
    enum class State {
        QUEUED,
        RUNNING,
        FINISHED
    }
}