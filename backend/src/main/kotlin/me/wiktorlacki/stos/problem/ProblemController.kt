package me.wiktorlacki.stos.problem

import me.wiktorlacki.stos.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/problems")
class ProblemController(
    private val userService: UserService,
    private val problemService: ProblemService
) {


    @GetMapping("/{problemId}")
    fun get(@PathVariable problemId: Long, authentication: Authentication): ResponseEntity<DetailedProblemDTO> {
        val response = problemService.getContextAwareByProblemId(authentication.name, problemId)

        return ResponseEntity.ok(response)
    }

    @PutMapping
    fun create(request: ProblemCreateRequest, authentication: Authentication): ResponseEntity<DetailedProblemDTO> {
        val response = problemService.create(authentication.name, request)

        return ResponseEntity.ok(response)
    }
}