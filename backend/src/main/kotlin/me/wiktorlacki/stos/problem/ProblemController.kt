package me.wiktorlacki.stos.problem

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/problems")
class ProblemController(private val problemService: ProblemService) {

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