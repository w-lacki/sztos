package me.wiktorlacki.ekomersz.auction

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
}