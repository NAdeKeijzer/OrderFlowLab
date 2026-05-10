package org.nikita.orderflowlab.order.dto

import java.math.BigDecimal
import java.util.*

data class OrderLineResponse(
    val productId: UUID,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val lineTotal: BigDecimal
)