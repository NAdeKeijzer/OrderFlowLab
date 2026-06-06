package org.nikita.orderflowlab.inventory.event

import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class InventoryReservedConsumer(
    private val inventoryReservedEventHandler: InventoryReservedEventHandler
) {

    @KafkaListener(
        topics = ["inventory.reserved"],
        groupId = "order-flow-lab",
        containerFactory = "inventoryReservedEventKafkaListenerContainerFactory"
    )
    fun consume(event: InventoryReservedEvent) {
        inventoryReservedEventHandler.handle(event)
    }
}