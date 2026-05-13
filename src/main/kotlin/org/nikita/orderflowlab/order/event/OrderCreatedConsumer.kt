package org.nikita.orderflowlab.order.event

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class OrderCreatedConsumer {

    @KafkaListener(
        topics = ["order.created"],
        groupId = "order-flow-lab",
        containerFactory = "orderCreatedEventKafkaListenerContainerFactory"
    )
    fun consume(event: OrderCreatedEvent) {
        println("Received order event: $event")
    }
}