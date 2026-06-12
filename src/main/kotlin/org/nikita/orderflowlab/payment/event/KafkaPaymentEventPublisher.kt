package org.nikita.orderflowlab.payment.event

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaPaymentEventPublisher(
    private val paymentRequestedKafkaTemplate: KafkaTemplate<String, PaymentRequestedEvent>,
    private val paymentSucceededKafkaTemplate: KafkaTemplate<String, PaymentSucceededEvent>
) : PaymentEventPublisher {

    private val logger = LoggerFactory.getLogger(KafkaPaymentEventPublisher::class.java)

    override fun publishPaymentRequested(event: PaymentRequestedEvent) {
        paymentRequestedKafkaTemplate.send(
            "payment.requested",
            event.orderId.toString(),
            event
        )

        logger.info(
            "Published payment requested event for orderId={}, amount={}",
            event.orderId,
            event.amount
        )
    }

    override fun publishPaymentSucceeded(event: PaymentSucceededEvent) {
        paymentSucceededKafkaTemplate.send(
            "payment.succeeded",
            event.orderId.toString(),
            event
        )

        logger.info(
            "Published payment succeeded event for orderId={}",
            event.orderId
        )
    }
}