package me.wiktorlacki.ekomersz.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.ekomersz.user.User
import me.wiktorlacki.ekomersz.user.UserService
import me.wiktorlacki.ekomersz.validate
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class EmailVerificationService(
    private val userService: UserService,
    private val otpService: OtpService,
    private val mailSender: MailSender
) {

    @Async
    fun sendVerificationEmail(user: User) {
        validate(!user.emailVerified) { "Email already verified ${user.emailVerified}" }
        val code = otpService.generateOtp(user)
        val emailVerificationUrl = "http://localhost:8080/api/auth/verify/${user.id}?code=${code}"
        val emailText = "Click the link to verify your email: $emailVerificationUrl"

        val message = SimpleMailMessage()
        message.setTo(user.email)
        message.subject = "Email Verification"
        message.from = "System"
        message.text = emailText

        mailSender.send(message)
    }

    @Transactional
    fun verifyEmail(id: UUID, code: String): VerificationResponse {
        if (!otpService.isValidOtp(id, code)) throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Invalid or expired verification code."
        )
        otpService.invalidateOtp(id)

        val user = userService.getById(id) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "User with id $id does not exist."
        )

        user.emailVerified = true

        return user.toVerificationResponse()
    }
}