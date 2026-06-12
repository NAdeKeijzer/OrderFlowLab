package org.nikita.orderflowlab.inventory.event

import org.nikita.orderflowlab.payment.event.PaymentEventPublisher
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class InventoryReservedEventHandler(
    private val paymentEventPublisher: PaymentEventPublisher
) {

    private val logger = LoggerFactory.getLogger(InventoryReservedEventHandler::class.java)

    fun handle(event: InventoryReservedEvent) {
        logger.info(
            "Handling inventory reserved event for orderId={}",
            event.orderId
        )

        paymentEventPublisher.publishPaymentRequested(
            PaymentRequestedEvent(
                orderId = event.orderId,
                customerId = event.customerId,
                amount = event.totalPrice,
                requestedAt = Instant.now()
            )
        )
    }
}