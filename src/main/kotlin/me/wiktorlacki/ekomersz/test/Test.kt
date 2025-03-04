package me.wiktorlacki.ekomersz.test

import jakarta.persistence.*
import me.wiktorlacki.ekomersz.problem.Problem
import me.wiktorlacki.ekomersz.submission.Submission
import me.wiktorlacki.ekomersz.user.User
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@Entity
@Table(name = "tests")
@EntityListeners(AuditingEntityListener::class)
class Test(
    @Id
    val id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "problem_id")
    val problem: Problem,

    @Column(nullable = false, updatable = false)
    val points: Int,

    @Column(nullable = false)
    val input: String,

    @Column(nullable = false)
    val output: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private var createdAt: Instant? = null,

    @Column(name = "updated_at", nullable = false, updatable = false)
    @LastModifiedDate
    private var updatedAt: Instant? = null
)