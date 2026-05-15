package org.nikita.orderflowlab.inventory

import jakarta.validation.Valid
import org.nikita.orderflowlab.inventory.dto.CreateInventoryItemRequest
import org.nikita.orderflowlab.inventory.dto.InventoryItemResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/inventory-items")
class InventoryController(
    private val inventoryItemRepository: InventoryItemRepository
) {

    @PostMapping
    fun createOrUpdate(
        @Valid @RequestBody request: CreateInventoryItemRequest
    ): InventoryItemResponse {
        val existingItem = inventoryItemRepository.findById(request.productId!!)
            .orElse(null)

        val item = if (existingItem == null) {
            InventoryItem(
                productId = request.productId,
                availableQuantity = request.availableQuantity
            )
        } else {
            existingItem.availableQuantity = request.availableQuantity
            existingItem
        }

        return InventoryItemResponse.from(
            inventoryItemRepository.save(item)
        )
    }

    @GetMapping("/{productId}")
    fun getByProductId(
        @PathVariable productId: UUID
    ): ResponseEntity<InventoryItemResponse> {
        val item = inventoryItemRepository.findById(productId)
            .orElse(null)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(InventoryItemResponse.from(item))
    }
}