package me.wiktorlacki.stos.user

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {

    @GetMapping("/me")
    fun me(authentication: Authentication): ResponseEntity<UserResponse> {
        val user = userService.getByUsername(authentication.name)
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("/roles")
    fun roles(authentication: Authentication): ResponseEntity<List<String>> {
        val user = userService.getByUsername(authentication.name)
        return ResponseEntity.ok(user.roles.map { it.name }.toList())
    }

    @GetMapping("/{name}")
    @PreAuthorize("hasRole('ROLE_TEACHER')")
    fun getByName(@PathVariable name: String): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(userService.getAllByUsernameContaining(name).map { it.toResponse() })
    }
}