package org.nikita.orderflowlab.order.dto

import org.nikita.orderflowlab.order.Order
import org.nikita.orderflowlab.order.OrderStatus
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val customerId: UUID,
    val status: OrderStatus,
    val createdAt: Instant,
    val lines: List<OrderLineResponse>
) {
    companion object {
        fun from(order: Order): OrderResponse =
            OrderResponse(
                id = order.id,
                customerId = order.customerId,
                status = order.status,
                createdAt = order.createdAt,
                lines = order.lines.map {
                    OrderLineResponse(
                        productId = it.productId,
                        quantity = it.quantity
                    )
                }
            )
    }
}

data class OrderLineResponse(
    val productId: UUID,
    val quantity: Int
)