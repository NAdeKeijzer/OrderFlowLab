package org.nikita.orderflowlab.order.dto

import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateOrderRequest(
    @field:NotNull
    val customerId: UUID
)