package me.wiktorlacki.ekomersz.role

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Role

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}