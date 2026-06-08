package org.nikita.orderflowlab.inventory.event

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaInventoryEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, InventoryReservedEvent>
) : InventoryEventPublisher {

    private val logger = LoggerFactory.getLogger(KafkaInventoryEventPublisher::class.java)

    override fun publishInventoryReserved(event: InventoryReservedEvent) {
        kafkaTemplate.send(
            "inventory.reserved",
            event.orderId.toString(),
            event
        )

        logger.info(
            "Published inventory reserved event for orderId={}",
            event.orderId
        )
    }
}