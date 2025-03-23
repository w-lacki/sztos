package me.wiktorlacki.stos.contest

import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ContestService(
    private val contestRepository: ContestRepository,
    private val userService: UserService
) {

    fun isAllowedContextAware(user: User, contest: Contest) =
        if (user.hasRole("TEACHER")) user.createdContests.contains(contest) else user.contests.contains(contest)

    fun getContextAwareById(user: User, id: Long): DetailedContestDTO {
        val contest = contestRepository.findByIdOrNull(id) ?: throw ContestNotFoundException()

        if (!isAllowedContextAware(user, contest)) {
            throw ContestNotAllowedException()
        }

        return contest.toDetailedContestDTO()
    }

    fun getById(id: Long) = contestRepository.findByIdOrNull(id) ?: throw ContestNotFoundException()


    fun getAllContextAware(user: User): List<ContestDTO> {
        val contests = if (user.hasRole("TEACHER")) {
            user.createdContests
        } else {
            user.contests
        }

        return contests.map { it.toDTO() }
    }

    fun create(creator: User, request: ContestCreateRequest): ContestCreateResponse {
        val contest = Contest(
            title = request.title,
            description = request.description,
            creator = creator
        )

        return contestRepository.save(contest).toCreateResponse()
    }

    fun addUserToContest(contestId: Long, requester: User, request: ContestAddUserRequest) {
        val contest = getById(contestId)
        if (contest.creator != requester) throw ContestNotAllowedToModifyException()

        val user = userService.getById(request.user)
        if (user.contests.contains(contest)) throw ContestAlreadyContainsUserException()

        contest.users.add(user)
        contestRepository.save(contest)
    }

    fun removeUserFromContest(contestId: Long, requester: User, request: ContestRemoveUserRequest) {
        val contest = getById(contestId)
        if (contest.creator != requester) throw ContestNotAllowedToModifyException()

        val user = userService.getById(request.user)
        if (!user.contests.contains(contest)) throw ContestDoesNotContainUserException()

        contest.users.remove(user)
        contestRepository.save(contest)
    }
}