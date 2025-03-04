package me.wiktorlacki.ekomersz.submission

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/submissions")
class SubmissionController(private val submissionService: SubmissionService) {

    @PostMapping
    fun submit(
        @RequestBody request: SubmissionCreateRequest,
        authentication: Authentication
    ): ResponseEntity<SubmissionStatus> {
        val response = submissionService.submit(authentication.name, request)

        return ResponseEntity.ok(response)
    }
}