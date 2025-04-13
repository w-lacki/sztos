package me.wiktorlacki.stos.auth

class EmailNotVerifiedException : Exception("Email not verified.")

class InvalidOTPException : Exception("Invalid or expired verification code.")

class RefreshTokenExpiredException : Exception("Refresh token has expired.")

class UsernameTakenException : Exception("Username is already taken.")

class EmailTakenException : Exception("Email is already taken.")