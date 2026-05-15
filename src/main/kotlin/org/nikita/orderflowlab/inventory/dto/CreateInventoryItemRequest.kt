package org.nikita.orderflowlab.inventory.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.*

data class CreateInventoryItemRequest(

    @field:NotNull
    val productId: UUID?,

    @field:Min(0)
    val availableQuantity: Int
)