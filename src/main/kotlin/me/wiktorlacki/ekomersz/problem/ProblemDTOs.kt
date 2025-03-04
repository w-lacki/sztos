package me.wiktorlacki.ekomersz.problem

import java.time.Instant


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