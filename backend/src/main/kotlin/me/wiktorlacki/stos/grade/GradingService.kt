package me.wiktorlacki.stos.grade

import me.wiktorlacki.stos.submission.Submission
import me.wiktorlacki.stos.test.Test
import me.wiktorlacki.stos.test.TestsNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.time.Duration
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists


@Service
class GradingService(
    private val gradeRepository: GradeRepository
) {

    @Value("\${container.time_out}")
    private lateinit var timeOut: Duration

    private val logger = LoggerFactory.getLogger(javaClass)

    private sealed class GradingStageResult(val message: String) {
        class Error(message: String) : GradingStageResult(message)
        class Success(message: String) : GradingStageResult(message)
    }

    @Async
    fun grade(tests: List<Test>, submission: Submission) {
        if (tests.isEmpty()) throw TestsNotFoundException()

        val submissionDir = Files.createTempDirectory("submission")
        val submissionFile = submissionDir.resolve("submission.cpp")

        Files.writeString(submissionFile, submission.sourceCode)

        // COMPILATION
        val compilationResult = compile(submissionDir)

        if (compilationResult is GradingStageResult.Error) {
            val errorMessage = compilationResult.message
            val grade = Grade(
                submission = submission,
                test = tests.first(),
                type = Grade.Type.COMPILATION_ERROR,
                points = 0,
                output = errorMessage
            )
            gradeRepository.save(grade)
            cleanup(submissionDir)
        }


        // TESTING
        test(tests, submission, submissionDir)

        cleanup(submissionDir)
    }

    private fun test(tests: List<Test>, submission: Submission, submissionDir: Path) {
        tests.forEach { test ->
            val executionResult = run(test.input, submissionDir)
            if (executionResult is GradingStageResult.Error) {
                val grade = Grade(
                    submission = submission,
                    test = test,
                    type = Grade.Type.RUNTIME_ERROR,
                    points = 0,
                    output = executionResult.message,
                )
                gradeRepository.save(grade)
                return@forEach
            }

            val userOutput = executionResult.message
            val points = if (compare(userOutput, test.output)) test.points else 0
            val result = Grade(
                submission = submission,
                test = test,
                type = Grade.Type.SUCCESS,
                points = points,
                output = userOutput,
            )
            gradeRepository.save(result)
        }

    }

    private fun compile(dir: Path): GradingStageResult {
        val process = ProcessBuilder(
            "docker", "run", "--rm",
            "-v", "$dir:/grading",
            "--network", "none",
            "--cap-drop=ALL",
            "--memory", "64m",
            "--cpus", "0.5",
            "grader",
            "timeout", "${timeOut.toSeconds()}",
            "g++",
            "-g", "-fsanitize=address", "-o", "submission",
            "submission.cpp"
        ).start()

        return awaitStageResult(process)
    }

    private fun run(input: String, dir: Path): GradingStageResult {
        val process = ProcessBuilder(
            "docker", "run", "--rm",
            "-i",
            "-v", "$dir:/grading",
            "--network", "none",
            "--cap-drop=ALL",
            "--memory", "64m",
            "--cpus", "0.5",
            "--pids-limit", "50",
            "grader",
            "timeout", "${timeOut.toSeconds()}",
            "./submission"
        ).start()

        process.outputStream.bufferedWriter().use {
            it.write(input)
            it.newLine()
            it.flush()
        }

        return awaitStageResult(process)
    }

    private fun awaitStageResult(process: Process): GradingStageResult {
        process.waitFor()
        return if (process.exitValue() == 0) {
            val message = process.inputStream.readAll()
            GradingStageResult.Success(message)
        } else {
            val error = process.errorStream.readAll()
            GradingStageResult.Error(error)
        }
    }

    private fun InputStream.readAll() = this.bufferedReader()
        .readLines()
        .joinToString("\n")

    private fun compare(userOutput: String, output: String): Boolean {
        return userOutput == output
    }

    private fun cleanup(submissionDir: Path) {
        Files.walk(submissionDir).filter { Files.isRegularFile(it) }.forEach { it.deleteIfExists() }
        submissionDir.deleteIfExists()
    }
}