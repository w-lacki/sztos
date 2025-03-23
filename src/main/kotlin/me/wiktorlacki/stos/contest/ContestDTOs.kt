package me.wiktorlacki.stos.contest

import jakarta.validation.constraints.NotBlank
import me.wiktorlacki.stos.problem.ProblemDTO
import me.wiktorlacki.stos.problem.toDTO
import java.time.Instant
import java.util.UUID

data class ContestCreateRequest(
    @NotBlank
    val title: String,
    @NotBlank
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