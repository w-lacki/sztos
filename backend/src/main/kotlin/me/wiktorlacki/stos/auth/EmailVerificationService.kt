package me.wiktorlacki.stos.auth

import jakarta.transaction.Transactional
import me.wiktorlacki.stos.user.User
import me.wiktorlacki.stos.user.UserService
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Service
class EmailVerificationService(
    private val userService: UserService,
    private val otpService: OtpService,
    private val mailSender: MailSender
) {

    @Async
    fun sendVerificationEmail(user: User) {
        if (user.emailVerified) throw EmailAlreadyVerifiedException()

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
        if (!otpService.isValidOtp(id, code)) throw InvalidOTPException()
        otpService.invalidateOtp(id)

        val user = userService.getById(id)

        user.emailVerified = true

        return user.toVerificationResponse()
    }
}