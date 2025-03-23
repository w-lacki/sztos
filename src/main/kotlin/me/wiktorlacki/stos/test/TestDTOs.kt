package me.wiktorlacki.stos.test

import me.wiktorlacki.stos.grade.GradeDTO

data class TestDTO(
    val id: Long,
    val points: Int,
    val input: String,
    val output: String
)

fun Test.toDTO() = TestDTO(id!!, points, input, output)


data class ResultsResponse(
    val tests: List<TestDTO>,
    val results: List<GradeDTO>
)