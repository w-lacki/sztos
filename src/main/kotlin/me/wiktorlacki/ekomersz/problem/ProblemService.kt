package me.wiktorlacki.ekomersz.problem

import me.wiktorlacki.ekomersz.contest.ContestNotAllowedException
import me.wiktorlacki.ekomersz.contest.ContestService
import me.wiktorlacki.ekomersz.user.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProblemService(
    private val problemRepository: ProblemRepository,
    private val userService: UserService,
    private val contestService: ContestService
) {

    fun getById(id: Long) = problemRepository.findByIdOrNull(id) ?: throw ProblemNotFoundException()

    fun getContextAwareByProblemId(username: String, problemId: Long): DetailedProblemDTO {
        val user = userService.getByUsername(username)
        val problem = problemRepository.findByIdOrNull(problemId) ?: throw ProblemNotFoundException()
        if (!contestService.isAllowedContextAware(user, problem.contest)) throw ContestNotAllowedException()

        return problem.toDetailedDTO()
    }
}