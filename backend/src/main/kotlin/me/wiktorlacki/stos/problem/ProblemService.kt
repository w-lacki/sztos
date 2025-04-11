package me.wiktorlacki.stos.problem

import me.wiktorlacki.stos.contest.ContestNotAllowedException
import me.wiktorlacki.stos.contest.ContestService
import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProblemService(
    private val problemRepository: ProblemRepository,
    private val userService: UserService,
    private val contestService: ContestService
) {

    fun getById(id: Long) = problemRepository.findByIdOrNull(id) ?: throw ProblemNotFoundException()

    fun getContextAwareByProblemId(userName: String, problemId: Long): DetailedProblemDTO {
        val problem = problemRepository.findByIdOrNull(problemId) ?: throw ProblemNotFoundException()
        val user = userService.getByUsername(userName)
        if (!contestService.isAllowedContextAware(user, problem.contest)) throw ContestNotAllowedException()

        return problem.toDetailedDTO()
    }

    fun create(creatorName: String, request: ProblemCreateRequest): DetailedProblemDTO {
        val contest = contestService.getById(request.contestId)
        val creator = userService.getByUsername(creatorName)
        if (creator != contest.creator) throw ContestNotAllowedException()

        val problem = Problem(
            title = request.title,
            description = request.description,
            contest = contest,
            deadline = request.deadline
        )

        return problemRepository.save(problem).toDetailedDTO()
    }
}