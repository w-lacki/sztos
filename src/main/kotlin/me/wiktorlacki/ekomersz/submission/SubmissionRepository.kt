package me.wiktorlacki.ekomersz.submission

import org.springframework.data.repository.CrudRepository

interface SubmissionRepository : CrudRepository<Submission, Long>