package org.nikita.orderflowlab.inventory.event

import org.nikita.orderflowlab.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class InventoryReservedEventHandler(
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(InventoryReservedEventHandler::class.java)

    fun handle(event: InventoryReservedEvent) {
        logger.info(
            "Handling inventory reserved event for orderId={}",
            event.orderId
        )

        orderService.confirm(event.orderId)
    }
}