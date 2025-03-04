package me.wiktorlacki.ekomersz.submission

import jakarta.validation.constraints.NotBlank

data class SubmissionCreateRequest(
    val problem: Long,
    @NotBlank
    val code: String
)

data class SubmissionStatus(
    val id: Long,
    val state: Submission.State
)

fun Submission.toSubmissionCreateResponse() = SubmissionStatus(id!!, state)