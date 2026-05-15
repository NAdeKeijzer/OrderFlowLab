package org.nikita.orderflowlab.inventory.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "inventory_items")
class InventoryItem(

    @Id
    val productId: UUID,

    @Column(nullable = false)
    var availableQuantity: Int,

    @Column(nullable = false)
    val updatedAt: Instant = Instant.now()
)