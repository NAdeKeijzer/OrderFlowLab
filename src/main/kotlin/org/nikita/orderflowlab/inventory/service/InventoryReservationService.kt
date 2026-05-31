package org.nikita.orderflowlab.inventory.service

import org.nikita.orderflowlab.inventory.exception.InsufficientInventoryException
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.model.InventoryReservation
import org.nikita.orderflowlab.inventory.repository.InventoryItemRepository
import org.nikita.orderflowlab.inventory.repository.InventoryReservationRepository
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class InventoryReservationService(
    private val inventoryReservationRepository: InventoryReservationRepository,
    private val inventoryItemRepository: InventoryItemRepository
) {

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

            inventoryItem.decreaseAvailableQuantity(line.quantity)

            inventoryReservationRepository.save(
                InventoryReservation(
                    orderId = event.orderId,
                    productId = line.productId,
                    quantity = line.quantity
                )
            )
        }
    }

    @Transactional
    fun releaseFor(orderId: UUID) {
        val reservations = inventoryReservationRepository
            .findAllByOrderId(orderId)

        reservations.forEach { reservation ->

            val inventoryItem = inventoryItemRepository.findById(
                reservation.productId
            ).orElseThrow {
                InventoryItemNotFoundException(reservation.productId)
            }

            inventoryItem.increaseAvailableQuantity(
                reservation.quantity
            )

            inventoryItemRepository.save(inventoryItem)
        }

        inventoryReservationRepository.deleteAll(reservations)
    }
}