package org.nikita.orderflowlab.inventory

import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InventoryReservationService {

    private val logger = LoggerFactory.getLogger(InventoryReservationService::class.java)

    fun reserveFor(event: OrderCreatedEvent) {
        logger.info(
            "Reserved inventory for orderId={}, totalPrice={}",
            event.orderId,
            event.totalPrice
        )
    }
}