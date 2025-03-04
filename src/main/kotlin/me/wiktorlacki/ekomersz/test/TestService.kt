package me.wiktorlacki.ekomersz.test

import me.wiktorlacki.ekomersz.submission.Submission
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists


@Service
class TestService(
    private val testResultRepository: TestResultRepository
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun compileAndRun(submission: Submission) {
        val submissionDir = Files.createTempDirectory("submission")
        val submissionFile = submissionDir.resolve("submission.cpp")

        logger.info(submissionDir.absolutePathString())

        Files.writeString(submissionFile, submission.content)

        // COMPILATION
        val compilationOutput = compile(submissionDir)
        logger.info(compilationOutput)


        // TESTING
        val tests = submission.problem.tests
        tests.forEach {
            val output = run(it.input, submissionDir)
            val points = if (compare(output, it.output)) it.points else 0

            val result = TestResult(
                submission = submission,
                points = points,
                output = output,
            )
            testResultRepository.save(result)
        }

        Files.walk(submissionDir).filter { Files.isRegularFile(it) }.forEach { it.deleteIfExists() }
        submissionDir.deleteIfExists()
    }

    fun compile(dir: Path): String {
        val pb = ProcessBuilder(
            "docker", "run", "--rm",
            "-v", "$dir:/grading",
            "--network", "none",
            "grader", "gcc", "-o", "submission", "submission.cpp"
        )

        pb.redirectErrorStream(false)

        val process = pb.start()
        process.waitFor()

        return if (process.exitValue() == 0) {
            readOutput(process.inputStream)
        } else readOutput(process.errorStream)
    }

    fun run(input: String, dir: Path): String {
        val pb = ProcessBuilder(
            "docker", "run", "--rm",
            "-i",
            "-v", "$dir:/grading",
            "--network", "none",
            "grader", "./submission"
        )

        val process = pb.start()

        process.outputStream.bufferedWriter().use {
            it.write(input)
            it.newLine()
            it.flush()
        }

        process.waitFor()

        return if (process.exitValue() == 0) {
            readOutput(process.inputStream)
        } else readOutput(process.errorStream)
    }

    fun compare(userOutput: String, output: String): Boolean {
        return userOutput == output
    }

    private fun readOutput(inputStream: InputStream): String {
        return inputStream.bufferedReader()
            .readLines()
            .joinToString("\n")
    }
}