package org.nikita.orderflowlab.inventory.event

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class InventoryReservedEvent(
    val orderId: UUID,
    val customerId: UUID,
    val totalPrice: BigDecimal,
    val reservedAt: Instant
)