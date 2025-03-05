package me.wiktorlacki.ekomersz.grade

import me.wiktorlacki.ekomersz.submission.Submission
import org.springframework.data.repository.CrudRepository

interface GradeRepository : CrudRepository<Grade, Long> {

    fun findBySubmission(submission: Submission): List<Grade>
}