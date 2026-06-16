package org.nikita.orderflowlab.payment.event

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class PaymentFailedConsumer(
    private val paymentFailedEventHandler: PaymentFailedEventHandler
) {

    @KafkaListener(
        topics = ["payment.failed"],
        groupId = "order-flow-lab",
        containerFactory = "paymentFailedEventKafkaListenerContainerFactory"
    )
    fun consume(event: PaymentFailedEvent) {
        paymentFailedEventHandler.handle(event)
    }
}