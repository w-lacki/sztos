package me.wiktorlacki.stos.submission

import jakarta.transaction.Transactional
import me.wiktorlacki.stos.contest.ContestNotAllowedException
import me.wiktorlacki.stos.problem.Problem
import me.wiktorlacki.stos.problem.ProblemService
import me.wiktorlacki.stos.grade.GradingService
import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserService
import org.springframework.stereotype.Service

@Service
class SubmissionService(
    private val userService: UserService,
    private val problemService: ProblemService,
    private val gradingService: GradingService,
    private val submissionRepository: SubmissionRepository
) {

    fun getLatestSubmissionByProblem(user: User, problem: Problem) =
        submissionRepository.findFirstByProblemAndSubmitterOrderByCreatedAtDesc(problem, user)

    @Transactional
    fun submit(username: String, request: SubmissionCreateRequest): SubmissionStatus {
        val submitter = userService.getByUsername(username)
        val problem = problemService.getById(request.problem)

        if (!submitter.contests.contains(problem.contest)) throw ContestNotAllowedException()

        val submission = Submission(
            submitter = submitter,
            problem = problem,
            state = Submission.State.RUNNING,
            sourceCode = request.code
        )

        submissionRepository.save(submission)
        val tests = submission.problem.tests
        tests.count() // Lazy load
        gradingService.grade(tests, submission)

        return submission.toSubmissionCreateResponse()
    }


}