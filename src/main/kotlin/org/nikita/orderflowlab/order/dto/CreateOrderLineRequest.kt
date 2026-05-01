package org.nikita.orderflowlab.order.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class CreateOrderLineRequest(

    @field:NotNull
    val productId: UUID?,

    @field:Min(1)
    val quantity: Int
)