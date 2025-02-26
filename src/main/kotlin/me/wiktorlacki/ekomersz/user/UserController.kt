package me.wiktorlacki.ekomersz.user

import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun me(authentication: Authentication): ResponseEntity<UserResponse> {
        val user = userService.getByUsername(authentication.name) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("/roles")
    fun roles(authentication: Authentication): ResponseEntity<List<String>> {
        val user = userService.getByUsername(authentication.name) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user.roles.map { it.name }.toList())
    }
}