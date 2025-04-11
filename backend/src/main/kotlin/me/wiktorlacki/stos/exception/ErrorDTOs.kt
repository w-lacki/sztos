package me.wiktorlacki.stos.exception

import java.time.Instant

data class DetailedErrorResponse(
    val timeStamp: String = Instant.now().toString(),
    val status: Int,
    val message: String,
    val errors: Map<String, String>,
)