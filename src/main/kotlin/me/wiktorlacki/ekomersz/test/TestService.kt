package me.wiktorlacki.ekomersz.test

import me.wiktorlacki.ekomersz.submission.Submission
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.PathWalkOption
import kotlin.io.path.deleteExisting
import kotlin.io.path.deleteIfExists
import kotlin.io.path.walk


@Service
class TestService {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun compileAndRun(submission: Submission) {
        val tempDir = Files.createTempDirectory("submission")
        val tempFile = tempDir.resolve("submission.cpp")
        Files.writeString(tempFile, submission.content)

        val output = execute(tempDir)
        logger.info("Na essie??")
        logger.info(output)

        Files.walk(tempDir).filter { Files.isRegularFile(it) }.forEach { it.deleteIfExists() }
        tempFile.deleteIfExists()
    }

    fun execute(dir: Path): String {
        val pb = ProcessBuilder(
            "docker", "run", "--rm",
            "-v", "$dir:/app",
            "--network", "none",
            "grader", "gcc", "-o", "solution", "solution.cpp"
        )

        pb.redirectErrorStream(false)

        val process = pb.start()
        process.waitFor()

        return if (process.exitValue() == 0) {
            readOutput(process.inputStream)
        } else readOutput(process.errorStream)
    }

    @Throws(IOException::class)
    private fun readOutput(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val output = StringBuilder()
        var line: String?
        while ((reader.readLine().also { line = it }) != null) {
            output.append(line).append("\n")
        }
        return output.toString()
    }
}