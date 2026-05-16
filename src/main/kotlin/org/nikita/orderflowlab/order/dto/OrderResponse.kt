package org.nikita.orderflowlab.order.dto

import org.nikita.orderflowlab.order.model.Order
import org.nikita.orderflowlab.order.model.OrderStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val customerId: UUID,
    val status: OrderStatus,
    val createdAt: Instant,
    val lines: List<OrderLineResponse>,
    val totalPrice: BigDecimal
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
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        lineTotal = it.total()
                    )
                },
                totalPrice = order.total()
            )
    }
}