package me.wiktorlacki.ekomersz.test

import me.wiktorlacki.ekomersz.problem.ProblemService
import me.wiktorlacki.ekomersz.submission.Submission
import me.wiktorlacki.ekomersz.submission.SubmissionService
import me.wiktorlacki.ekomersz.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@Service
@ControllerAdvice
class TestResultsService(
    private val testResultRepository: TestResultRepository,
    private val userService: UserService,
    private val submissionService: SubmissionService,
    private val problemService: ProblemService
) {

    fun getResults(username: String, problemId: Long): ResultsResponse {
        val user = userService.getByUsername(username)
        val problem = problemService.getById(problemId)
        val tests = problem.tests
        val testDTOs = tests.map { it.toDTO() }
        val submission = submissionService.getLatestSubmissionByProblem(user, problem) ?: let {
            return ResultsResponse(testDTOs, emptyList())
        }
        val results = testResultRepository.findBySubmission(submission).ifEmpty { throw ResultsNotReadyException() }
        return ResultsResponse(testDTOs, results.map { it.toDTO() })
    }

    @ExceptionHandler(ResultsNotReadyException::class)
    fun handleResultsNotReadyException(ex: ResultsNotReadyException): ResponseEntity<Void> {
        return ResponseEntity(HttpStatus.PARTIAL_CONTENT)
    }
}