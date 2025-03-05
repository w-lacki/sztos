package me.wiktorlacki.ekomersz.test

import me.wiktorlacki.ekomersz.grade.GradeDTO

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