package me.wiktorlacki.stos.contest

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import me.wiktorlacki.stos.problem.ProblemDTO
import me.wiktorlacki.stos.problem.toDTO
import java.time.Instant
import java.util.UUID

data class ContestCreateRequest(
    @get:NotBlank(message = "Title cannot be blank.")
    @get:Size(min = 3, max = 64, message = "Title must be between 3 and 64 characters.")
    val title: String,

    @get:NotBlank(message = "Description cannot be blank.")
    @get:Size(min = 1, max = 1024, message = "Description must be between 1 and 1024 characters.")
    val description: String,
)

data class ContestCreateResponse(
    val id: Long,
    val title: String,
    val description: String,
    val createdAt: Instant
)

fun Contest.toCreateResponse() = ContestCreateResponse(id!!, title, description, createdAt!!)

data class ContestDTO(
    val id: Long,
    val title: String,
    val createdAt: Instant,
)

fun Contest.toDTO() = ContestDTO(
    id!!,
    title,
    createdAt!!
)

data class ContestAddUserRequest(
    val user: UUID
)

data class ContestRemoveUserRequest(
    val user: UUID
)

data class DetailedContestDTO(
    val id: Long,
    val title: String,
    val description: String,
    val problems: List<ProblemDTO>,
)

fun Contest.toDetailedContestDTO() = DetailedContestDTO(
    id!!,
    title,
    description,
    problems.map { it.toDTO() }
)