package me.wiktorlacki.ekomersz.exception

import java.time.Instant

data class ErrorDetailsResponse(
    val message: String?,
    val timeStamp: Instant = Instant.now(),
)