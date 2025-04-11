package me.wiktorlacki.stos.test

import me.wiktorlacki.stos.contest.ContestNotAllowedException
import me.wiktorlacki.stos.grade.GradeRepository
import me.wiktorlacki.stos.grade.toDTO
import me.wiktorlacki.stos.problem.ProblemService
import me.wiktorlacki.stos.submission.SubmissionService
import me.wiktorlacki.stos.user.UserService
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ControllerAdvice

@Service
@ControllerAdvice
class TestService(
    private val gradeRepository: GradeRepository,
    private val testRepository: TestRepository,
    private val submissionService: SubmissionService,
    private val userService: UserService,
    private val problemService: ProblemService
) {

    fun getResults(userName: String, problemId: Long): ResultsResponse {
        val problem = problemService.getById(problemId)

        val user = userService.getByUsername(userName)
        if (!user.contests.contains(problem.contest)) throw ContestNotAllowedException()

        val tests = problem.tests
        val testDTOs = tests.map { it.toDTO() }
        val submission = submissionService.getLatestSubmissionByProblem(user, problem) ?: let {
            return ResultsResponse(emptyList(), emptyList())
        }

        val results = gradeRepository.findBySubmission(submission).ifEmpty { throw ResultsNotReadyException() }
        return ResultsResponse(testDTOs, results.map { it.toDTO() })
    }

    fun create(creatorName: String, request: TestCreateRequest): TestDTO {
        val problem = problemService.getById(request.problemId)

        val creator = userService.getByUsername(creatorName)
        if (creator != problem.contest.creator) throw ContestNotAllowedException()

        val test = Test(
            problem = problem,
            points = request.points,
            input = request.input,
            output = request.output
        )

        return testRepository.save(test).toDTO()
    }
}