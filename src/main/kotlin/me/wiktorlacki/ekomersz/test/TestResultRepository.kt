package me.wiktorlacki.ekomersz.test

import me.wiktorlacki.ekomersz.submission.Submission
import org.springframework.data.repository.CrudRepository

interface TestResultRepository : CrudRepository<TestResult, Long> {

    fun findBySubmission(submission: Submission): List<TestResult>
}