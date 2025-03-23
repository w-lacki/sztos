package me.wiktorlacki.stos.exception

import jakarta.validation.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class
GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(exception: AuthenticationException): ResponseEntity<DetailedErrorResponse> {
        return ResponseEntity.badRequest().body(
            DetailedErrorResponse(
                status = HttpStatus.BAD_REQUEST.value(),
                message = "Authentication failed",
                errors = mapOf("error" to exception.message.orEmpty())
            )
        )
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(exception: ValidationException): ResponseEntity<DetailedErrorResponse> {
        val errorResponse = DetailedErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation failed",
            errors = mapOf("error" to exception.message.orEmpty())
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(exception: MethodArgumentNotValidException): ResponseEntity<DetailedErrorResponse> {
        val errors = mutableMapOf<String, String>()
        exception.bindingResult.fieldErrors.forEach { error ->
            errors[error.field] = error.defaultMessage ?: "Validation error"
        }
        val errorResponse = DetailedErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = "Validation failed",
            errors = errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse)
    }
}