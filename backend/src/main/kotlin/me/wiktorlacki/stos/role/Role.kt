package me.wiktorlacki.stos.role

import jakarta.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String
)