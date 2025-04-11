package me.wiktorlacki.stos.test

import me.wiktorlacki.stos.user.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tests")
class TestController(
    private val userService: UserService,
    private val testService: TestService
) {

    @GetMapping("/results/{problemId}")
    fun results(
        @PathVariable problemId: Long,
        authentication: Authentication
    ): ResponseEntity<ResultsResponse> {
        val response = testService.getResults(authentication.name, problemId)

        return ResponseEntity.ok(response)
    }

    @PostMapping
    fun create(@RequestBody request: TestCreateRequest, authentication: Authentication): ResponseEntity<TestDTO> {
        val response = testService.create(authentication.name, request)

        return ResponseEntity.ok(response)
    }
}