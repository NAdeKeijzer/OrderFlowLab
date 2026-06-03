package org.nikita.orderflowlab.inventory.event

import java.time.Instant
import java.util.*

data class InventoryReservedEvent(
    val orderId: UUID,
    val reservedAt: Instant
)