package org.nikita.orderflowlab.inventory.service

import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.model.InventoryItem
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class InventoryService(
    private val inventoryItemRepository: InventoryItemRepository
) {

    fun createOrUpdateItem(
        productId: UUID,
        availableQuantity: Int
    ): InventoryItem {
        val existingItem = inventoryItemRepository.findById(productId)
            .orElse(null)

        val item = if (existingItem == null) {
            InventoryItem(
                productId = productId,
                availableQuantity = availableQuantity
            )
        } else {
            existingItem.availableQuantity = availableQuantity
            existingItem
        }

        return inventoryItemRepository.save(item)
    }

    fun getItem(productId: UUID): InventoryItem =
        inventoryItemRepository.findById(productId)
            .orElseThrow { InventoryItemNotFoundException(productId) }
}