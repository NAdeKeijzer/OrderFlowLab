package org.nikita.orderflowlab.order.event

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderCreatedEvent(
    val orderId: UUID,
    val customerId: UUID,
    val totalPrice: BigDecimal,
    val createdAt: Instant,
    val lines: List<OrderCreatedLineEvent>
)

data class OrderCreatedLineEvent(
    val productId: UUID,
    val quantity: Int
)