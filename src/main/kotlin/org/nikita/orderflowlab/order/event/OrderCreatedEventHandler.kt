package org.nikita.orderflowlab.order.event

import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderCreatedEventHandler(
    private val inventoryReservationService: InventoryReservationService
) {

    private val logger = LoggerFactory.getLogger(OrderCreatedEventHandler::class.java)

    fun handle(event: OrderCreatedEvent) {
        logger.info(
            "Handling order created event for orderId={}",
            event.orderId
        )

        inventoryReservationService.reserveFor(event)
    }
}