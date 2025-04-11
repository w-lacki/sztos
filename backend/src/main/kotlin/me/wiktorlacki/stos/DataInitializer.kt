package me.wiktorlacki.stos

import io.netty.util.internal.ThreadLocalRandom
import jakarta.transaction.Transactional
import me.wiktorlacki.stos.contest.ContestAddUserRequest
import me.wiktorlacki.stos.contest.ContestCreateRequest
import me.wiktorlacki.stos.contest.ContestService
import me.wiktorlacki.stos.problem.ProblemCreateRequest
import me.wiktorlacki.stos.problem.ProblemService
import me.wiktorlacki.stos.role.Role
import me.wiktorlacki.stos.role.RoleRepository
import me.wiktorlacki.stos.test.TestCreateRequest
import me.wiktorlacki.stos.test.TestService
import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class DataInitializer(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val contestService: ContestService,
    private val problemService: ProblemService,
    private val roleRepository: RoleRepository,
    private val testService: TestService,
) : ApplicationRunner {

    @Value("\${data.load_example}")
    private var loadExampleData = false

    @Transactional
    override fun run(args: ApplicationArguments?) {
        if (!loadExampleData) return

        setupRoles()
        setupUsers()
        setupContests()
    }

    private fun setupRoles() {
        listOf(Role(name = "ROLE_USER"), Role(name = "ROLE_TEACHER")).forEach {
            roleRepository.findByName(it.name) ?: roleRepository.save(it)
        }
    }

    private fun setupUsers() {
        val userRole = roleRepository.findByName("ROLE_USER") ?: error("role USER not found")
        val teacherRole = roleRepository.findByName("ROLE_TEACHER") ?: error("role TEACHER not found")

        userRepository.findByUsername("admin") ?: let {
            val user = User(
                username = "admin",
                email = "admin@example.org",
                password = passwordEncoder.encode("secret"),
                roles = mutableSetOf(userRole, teacherRole),
                emailVerified = true
            )
            userRepository.save(user)
        }

        userRepository.findByUsername("test") ?: let {
            val user = User(
                username = "test",
                email = "test@example.org",
                password = passwordEncoder.encode("secret"),
                roles = mutableSetOf(userRole),
                emailVerified = true
            )
            userRepository.save(user)
        }
    }

    private fun setupContests() {
        val title = "Algorithms and data structures"
        if (contestService.getByTitle(title).isNotEmpty()) return

        val admin = userRepository.findByUsername("admin") ?: error("Admin user not found")
        val contestCreationResponse = contestService.create(
            creatorName = admin.username,
            request = ContestCreateRequest(
                title = title,
                description = "In this contest you are going to solve complex problems"
            )
        )
        val problemCreationRequest = problemService.create(
            creatorName = admin.username,
            request = ProblemCreateRequest(
                title = "Is even?",
                description = "Write a program that reads an integer K, then reads K integers—one per line. For each integer, print 0 if it is odd, or 1 if it is even.",
                contestId = contestCreationResponse.id,
                deadline = Instant.now() + Duration.ofDays(31),
            )
        )

        val k = 5
        val input = buildString {
            appendLine(k)
            repeat(k) {
                appendLine("${ThreadLocalRandom.current().nextInt(1, 100_000)}")
            }
        }.trimEnd()

        val output = input.lines()
            .drop(1)
            .joinToString(separator = "\n") { if (it.toInt() % 2 == 0) "1" else "0" }
        testService.create(
            creatorName = admin.username,
            request = TestCreateRequest(
                problemId = problemCreationRequest.id,
                points = 10,
                input = input,
                output = output,
            )
        )

        val user = userRepository.findByUsername("test") ?: error("User not found")
        contestService.addUserToContest(
            contestId = contestCreationResponse.id,
            requesterName = admin.username,
            request = ContestAddUserRequest(user.id!!)
        )
    }
}