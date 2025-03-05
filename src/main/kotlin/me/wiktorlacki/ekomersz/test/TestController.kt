package me.wiktorlacki.ekomersz.test

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tests")
class TestController(
    private val testResultsService: TestResultsService
) {

    @GetMapping("/results/{problemId}")
    fun results(
        @PathVariable problemId: Long,
        authentication: Authentication
    ): ResponseEntity<ResultsResponse> {
        val response = testResultsService.getResults(authentication.name, problemId)

        return ResponseEntity.ok(response)
    }
}