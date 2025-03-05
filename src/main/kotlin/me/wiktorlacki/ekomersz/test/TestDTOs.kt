package me.wiktorlacki.ekomersz.test

data class TestDTO(
    val id: Long,
    val points: Int,
    val input: String,
    val output: String
)

fun Test.toDTO() = TestDTO(id!!, points, input, output)

data class TestResultDTO(
    val id: Long,
    val testId: Long,
    val points: Int,
    val output: String,
)

fun TestResult.toDTO() = TestResultDTO(id!!, test.id!!, points, output)

data class ResultsResponse(
    val tests: List<TestDTO>,
    val results: List<TestResultDTO>
)