package org.nikita.orderflowlab.inventory

import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryReservationService(
    private val inventoryReservationRepository: InventoryReservationRepository,
    private val inventoryItemRepository: InventoryItemRepository
) {

    private val logger = LoggerFactory.getLogger(InventoryReservationService::class.java)

    @Transactional
    fun reserveFor(event: OrderCreatedEvent) {
        event.lines.forEach { line ->
            val inventoryItem = inventoryItemRepository.findById(line.productId)
                .orElseThrow {
                    InventoryItemNotFoundException(line.productId)
                }

            if (inventoryItem.availableQuantity < line.quantity) {
                throw InsufficientInventoryException(
                    productId = line.productId,
                    requestedQuantity = line.quantity,
                    availableQuantity = inventoryItem.availableQuantity
                )
            }

            inventoryItem.availableQuantity -= line.quantity

            inventoryReservationRepository.save(
                InventoryReservation(
                    orderId = event.orderId,
                    productId = line.productId,
                    quantity = line.quantity
                )
            )

            logger.info(
                "Reserved inventory for orderId={}, productId={}, quantity={}",
                event.orderId,
                line.productId,
                line.quantity
            )
        }
    }
}