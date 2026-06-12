package org.nikita.orderflowlab.payment.event

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class PaymentRequestedEventHandler(
    private val paymentEventPublisher: PaymentEventPublisher
) {

    private val logger = LoggerFactory.getLogger(PaymentRequestedEventHandler::class.java)

    fun handle(event: PaymentRequestedEvent) {
        logger.info(
            "Handling payment requested event for orderId={}, amount={}",
            event.orderId,
            event.amount
        )

        paymentEventPublisher.publishPaymentSucceeded(
            PaymentSucceededEvent(
                orderId = event.orderId,
                paidAt = Instant.now()
            )
        )
    }
}