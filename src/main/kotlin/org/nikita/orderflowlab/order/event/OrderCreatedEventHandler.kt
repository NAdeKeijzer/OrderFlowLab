package org.nikita.orderflowlab.order.event

import org.nikita.orderflowlab.inventory.exception.InventoryDomainException
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderCreatedEventHandler(
    private val inventoryReservationService: InventoryReservationService,
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(OrderCreatedEventHandler::class.java)

    fun handle(event: OrderCreatedEvent) {
        logger.info(
            "Handling order created event for orderId={}",
            event.orderId
        )

        try {
            inventoryReservationService.reserveFor(event)

            orderService.markInventoryReserved(event.orderId)
            orderService.confirm(event.orderId)

            logger.info(
                "Inventory reservation succeeded for orderId={}",
                event.orderId
            )
        } catch (ex: InventoryDomainException) {

            logger.error(
                "Inventory reservation failed for orderId={}",
                event.orderId,
                ex
            )

            orderService.markInventoryFailed(event.orderId)
        }
    }
}