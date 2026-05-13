package org.nikita.orderflowlab.inventory

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "inventory_reservations")
class InventoryReservation(

    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val orderId: UUID,

    @Column(nullable = false)
    val productId: UUID,

    @Column(nullable = false)
    val quantity: Int,

    @Column(nullable = false)
    val reservedAt: Instant = Instant.now()
)