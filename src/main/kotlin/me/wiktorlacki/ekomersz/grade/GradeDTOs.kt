package me.wiktorlacki.ekomersz.grade

data class GradeDTO(
    val id: Long,
    val testId: Long,
    val type: Grade.Type,
    val points: Int,
    val output: String,
)

fun Grade.toDTO() = GradeDTO(id!!, test.id!!, type, points, output)