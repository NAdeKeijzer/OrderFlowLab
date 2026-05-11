package org.nikita.orderflowlab.order.event

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaOrderEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, OrderCreatedEvent>
) : OrderEventPublisher {

    override fun publishOrderCreated(event: OrderCreatedEvent) {
        kafkaTemplate.send(
            "order.created",
            event.orderId.toString(),
            event
        )
    }
}