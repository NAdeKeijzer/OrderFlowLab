package org.nikita.orderflowlab.order.dto

import java.util.UUID

data class OrderLineResponse(
    val productId: UUID,
    val quantity: Int
)