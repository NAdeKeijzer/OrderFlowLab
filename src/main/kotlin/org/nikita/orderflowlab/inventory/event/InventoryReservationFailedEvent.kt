package org.nikita.orderflowlab.inventory.event

import java.time.Instant
import java.util.*

data class InventoryReservationFailedEvent(
    val orderId: UUID,
    val failedAt: Instant,
    val reason: String
)