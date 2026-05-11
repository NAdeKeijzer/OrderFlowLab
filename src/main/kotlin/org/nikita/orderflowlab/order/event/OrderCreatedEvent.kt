package org.nikita.orderflowlab.order.event

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class OrderCreatedEvent(
    val orderId: UUID,
    val customerId: UUID,
    val totalPrice: BigDecimal,
    val createdAt: Instant
)