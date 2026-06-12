package org.nikita.orderflowlab.payment.event

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class PaymentRequestedConsumer(
    private val paymentRequestedEventHandler: PaymentRequestedEventHandler
) {

    @KafkaListener(
        topics = ["payment.requested"],
        groupId = "order-flow-lab",
        containerFactory = "paymentRequestedEventKafkaListenerContainerFactory"
    )
    fun consume(event: PaymentRequestedEvent) {
        paymentRequestedEventHandler.handle(event)
    }
}