package org.nikita.orderflowlab.order.model

import java.math.BigDecimal
import java.util.UUID

data class OrderLineInput(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal
)