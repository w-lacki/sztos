package me.wiktorlacki.stos.contest

import org.springframework.data.repository.CrudRepository

interface ContestRepository : CrudRepository<Contest, Long> {

    fun findByTitle(title: String): List<Contest>
}