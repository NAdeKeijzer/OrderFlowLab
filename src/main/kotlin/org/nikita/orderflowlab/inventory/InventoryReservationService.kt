package org.nikita.orderflowlab.inventory

import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryReservationService(
    private val inventoryReservationRepository: InventoryReservationRepository
) {

    private val logger = LoggerFactory.getLogger(InventoryReservationService::class.java)

    @Transactional
    fun reserveFor(event: OrderCreatedEvent) {
        event.lines.forEach { line ->
            val reservation = InventoryReservation(
                orderId = event.orderId,
                productId = line.productId,
                quantity = line.quantity
            )

            inventoryReservationRepository.save(reservation)

            logger.info(
                "Reserved inventory for orderId={}, productId={}, quantity={}",
                event.orderId,
                line.productId,
                line.quantity
            )
        }
    }
}