package org.nikita.orderflowlab.inventory.dto

import org.nikita.orderflowlab.inventory.InventoryItem
import java.time.Instant
import java.util.UUID

data class InventoryItemResponse(
    val productId: UUID,
    val availableQuantity: Int,
    val updatedAt: Instant
) {
    companion object {
        fun from(item: InventoryItem): InventoryItemResponse =
            InventoryItemResponse(
                productId = item.productId,
                availableQuantity = item.availableQuantity,
                updatedAt = item.updatedAt
            )
    }
}