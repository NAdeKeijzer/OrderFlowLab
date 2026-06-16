package org.nikita.orderflowlab.payment.event

import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.service.OrderService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PaymentFailedEventHandler(
    private val inventoryReservationService: InventoryReservationService,
    private val orderService: OrderService
) {

    private val logger = LoggerFactory.getLogger(PaymentFailedEventHandler::class.java)

    fun handle(event: PaymentFailedEvent) {
        logger.info(
            "Handling payment failed event for orderId={}, reason={}",
            event.orderId,
            event.reason
        )

        inventoryReservationService.releaseFor(event.orderId)
        orderService.markPaymentFailed(event.orderId)
    }
}