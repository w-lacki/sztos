package me.wiktorlacki.stos.grade

import me.wiktorlacki.stos.submission.Submission
import org.springframework.data.repository.CrudRepository

interface GradeRepository : CrudRepository<Grade, Long> {

    fun findBySubmission(submission: Submission): List<Grade>
}