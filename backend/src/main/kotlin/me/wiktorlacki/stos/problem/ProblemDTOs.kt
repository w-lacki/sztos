package me.wiktorlacki.stos.problem

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant

data class ProblemCreateRequest(
    val contestId: Long,

    @get:NotBlank(message = "Title cannot be blank.")
    @get:Size(min = 3, max = 64, message = "Title must be between 3 and 64 characters.")
    val title: String,

    @get:NotBlank(message = "Description cannot be blank.")
    @get:Size(min = 1, max = 1024, message = "Description must be between 1 and 1024 characters.")
    val description: String,

    @get:Future(message = "Deadline must be in the future.")
    val deadline: Instant
)

data class ProblemDTO(
    val id: Long,
    val title: String,
    val deadline: Instant
)

fun Problem.toDTO() = ProblemDTO(
    id!!,
    title,
    deadline
)

data class DetailedProblemDTO(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: Instant
)

fun Problem.toDetailedDTO() = DetailedProblemDTO(
    id!!, title, description, deadline
)