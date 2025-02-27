package me.wiktorlacki.ekomersz.user.auth

import me.wiktorlacki.ekomersz.user.User
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

@Service
class OtpService(private val redisTemplate: RedisTemplate<String, String>) {

    private val charset = "abcdefghijklmnopqrstuwxyz0123456789"

    fun isValidOtp(id: UUID, code: String) = redisTemplate.opsForValue().get(id.toCacheKey()) == code

    fun generateOtp(user: User) {
        val code = randomCode()
        redisTemplate.opsForValue().set(user.id.toCacheKey(), code, Duration.ofMinutes(5))
    }

    fun invalidateOtp(id: UUID) {
        redisTemplate.delete(id.toCacheKey())
    }

    private fun randomCode() = buildString {
        repeat(16) { append(charset.random()) }
    }


    private fun UUID?.toCacheKey() = "otp:${this}"
}
