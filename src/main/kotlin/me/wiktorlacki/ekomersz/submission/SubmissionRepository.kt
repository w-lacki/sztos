package me.wiktorlacki.ekomersz.submission

import me.wiktorlacki.ekomersz.problem.Problem
import me.wiktorlacki.ekomersz.user.User
import org.springframework.data.repository.CrudRepository

interface SubmissionRepository : CrudRepository<Submission, Long> {
    fun findFirstByProblemAndSubmitterOrderByCreatedAtDesc(problem: Problem, submitter: User): Submission?
}