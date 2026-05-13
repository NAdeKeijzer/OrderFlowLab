package org.nikita.orderflowlab.order.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Min
import java.math.BigDecimal
import java.util.UUID

data class CreateOrderLineRequest(

    val productId: UUID,

    @field:Min(1)
    val quantity: Int,

    @field:DecimalMin("0.01")
    val unitPrice: BigDecimal
)