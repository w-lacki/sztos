package me.wiktorlacki.stos.submission

import me.wiktorlacki.stos.problem.Problem
import me.wiktorlacki.stos.user.User
import org.springframework.data.repository.CrudRepository

interface SubmissionRepository : CrudRepository<Submission, Long> {
    fun findFirstByProblemAndSubmitterOrderByCreatedAtDesc(problem: Problem, submitter: User): Submission?
}