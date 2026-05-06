package org.nikita.orderflowlab.order

import java.util.UUID

data class OrderLineInput(
    val productId: UUID,
    val quantity: Int
)