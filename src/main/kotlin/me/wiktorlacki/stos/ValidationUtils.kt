package me.wiktorlacki.stos

import jakarta.validation.ValidationException

fun validate(condition: Boolean, message: () -> String) {
    if (!condition) throw ValidationException(message())
}

fun <T> validateNotNull(value: T?, message: () -> String) {
    if (value == null) throw ValidationException(message())
}
