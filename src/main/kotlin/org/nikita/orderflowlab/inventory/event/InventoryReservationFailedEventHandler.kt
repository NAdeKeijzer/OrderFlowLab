package org.nikita.orderflowlab.inventory.event

import org.nikita.orderflowlab.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InventoryReservationFailedEventHandler(
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(InventoryReservationFailedEventHandler::class.java)

    fun handle(event: InventoryReservationFailedEvent) {
        logger.info(
            "Handling inventory reservation failed event for orderId={}, reason={}",
            event.orderId,
            event.reason
        )

        orderService.markInventoryFailed(event.orderId)
    }
}