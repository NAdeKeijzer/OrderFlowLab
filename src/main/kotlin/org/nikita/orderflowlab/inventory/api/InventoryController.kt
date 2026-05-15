package org.nikita.orderflowlab.inventory.api

import jakarta.validation.Valid
import org.nikita.orderflowlab.inventory.dto.CreateInventoryItemRequest
import org.nikita.orderflowlab.inventory.dto.InventoryItemResponse
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.service.InventoryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/inventory-items")
class InventoryController(
    private val inventoryService: InventoryService
) {

    @PostMapping
    fun createOrUpdate(
        @Valid @RequestBody request: CreateInventoryItemRequest
    ): InventoryItemResponse {
        val item = inventoryService.createOrUpdateItem(
            productId = request.productId!!,
            availableQuantity = request.availableQuantity
        )

        return InventoryItemResponse.from(item)
    }

    @GetMapping("/{productId}")
    fun getByProductId(
        @PathVariable productId: UUID
    ): ResponseEntity<InventoryItemResponse> =
        try {
            ResponseEntity.ok(
                InventoryItemResponse.from(
                    inventoryService.getItem(productId)
                )
            )
        } catch (_: InventoryItemNotFoundException) {
            ResponseEntity.notFound().build()
        }
}