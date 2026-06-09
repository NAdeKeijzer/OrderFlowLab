package org.nikita.orderflowlab.inventory.event

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaInventoryEventPublisher(
    private val inventoryReservedKafkaTemplate: KafkaTemplate<String, InventoryReservedEvent>,
    private val inventoryReservationFailedKafkaTemplate: KafkaTemplate<String, InventoryReservationFailedEvent>
) : InventoryEventPublisher {

    private val logger = LoggerFactory.getLogger(KafkaInventoryEventPublisher::class.java)

    override fun publishInventoryReserved(event: InventoryReservedEvent) {
        inventoryReservedKafkaTemplate.send(
            "inventory.reserved",
            event.orderId.toString(),
            event
        )

        logger.info(
            "Published inventory reserved event for orderId={}",
            event.orderId
        )
    }

    override fun publishInventoryReservationFailed(event: InventoryReservationFailedEvent) {
        inventoryReservationFailedKafkaTemplate.send(
            "inventory.reservation-failed",
            event.orderId.toString(),
            event
        )

        logger.info(
            "Published inventory reservation failed event for orderId={}",
            event.orderId
        )
    }
}