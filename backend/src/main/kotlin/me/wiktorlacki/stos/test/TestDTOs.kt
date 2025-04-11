package me.wiktorlacki.stos.test

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size
import me.wiktorlacki.stos.grade.GradeDTO

data class TestCreateRequest(
    val problemId: Long,

    @get:PositiveOrZero
    val points: Int,
    @get:NotBlank

    //16,384 ascii characters in UTF-8 is 16KB which should be enough for an input
    @get:NotBlank
    @get:Size(min = 1, max = 16_384, message = "Input must be between 1 and 16,384 characters.")
    val input: String,

    //16,384 ascii characters in UTF-8 is 16KB which should be enough for an output
    @get:NotBlank
    @get:Size(min = 1, max = 16_384, message = "Output must be between 1 and 16,384 characters.")
    val output: String
)

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