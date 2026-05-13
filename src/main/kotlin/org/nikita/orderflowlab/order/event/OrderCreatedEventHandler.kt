package org.nikita.orderflowlab.order.event

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class OrderCreatedEventHandler {

    private val logger = LoggerFactory.getLogger(OrderCreatedEventHandler::class.java)

    fun handle(event: OrderCreatedEvent) {
        logger.info(
            "Received order created event for orderId={}, customerId={}, totalPrice={}",
            event.orderId,
            event.customerId,
            event.totalPrice
        )
    }
}