package me.wiktorlacki.stos.grade

import jakarta.persistence.*
import me.wiktorlacki.stos.submission.Submission
import me.wiktorlacki.stos.test.Test
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "grades")
@EntityListeners(AuditingEntityListener::class)
class Grade(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "submission_id")
    val submission: Submission,

    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id")
    val test: Test,

    @Column(nullable = false, updatable = false)
    val type: Type,

    @Column(nullable = false, updatable = false)
    val points: Int,

    @Column(nullable = false, columnDefinition = "TEXT")
    val output: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private var createdAt: Instant? = null,
) {
    enum class Type {
        COMPILATION_ERROR, RUNTIME_ERROR, SUCCESS
    }
}