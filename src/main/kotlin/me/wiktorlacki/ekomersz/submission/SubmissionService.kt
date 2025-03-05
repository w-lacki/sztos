package me.wiktorlacki.ekomersz.submission

import jakarta.transaction.Transactional
import me.wiktorlacki.ekomersz.contest.ContestNotAllowedException
import me.wiktorlacki.ekomersz.problem.Problem
import me.wiktorlacki.ekomersz.problem.ProblemService
import me.wiktorlacki.ekomersz.test.TestService
import me.wiktorlacki.ekomersz.user.User
import me.wiktorlacki.ekomersz.user.UserService
import org.springframework.stereotype.Service

@Service
class SubmissionService(
    private val userService: UserService,
    private val problemService: ProblemService,
    private val testService: TestService,
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
            content = request.code
        )

        submissionRepository.save(submission)

        testService.compileAndRun(submission)

        return submission.toSubmissionCreateResponse()
    }

}