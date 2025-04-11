package me.wiktorlacki.stos.role

import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<Role, Long> {
    fun findByName(name: String): Role?
}