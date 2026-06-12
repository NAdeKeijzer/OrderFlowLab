package org.nikita.orderflowlab.payment.event

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class PaymentSucceededConsumer(
    private val paymentSucceededEventHandler: PaymentSucceededEventHandler
) {

    @KafkaListener(
        topics = ["payment.succeeded"],
        groupId = "order-flow-lab",
        containerFactory = "paymentSucceededEventKafkaListenerContainerFactory"
    )
    fun consume(event: PaymentSucceededEvent) {
        paymentSucceededEventHandler.handle(event)
    }
}