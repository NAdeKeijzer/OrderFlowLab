package org.nikita.orderflowlab.inventory.event

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class NoOpInventoryEventPublisher {

    @Bean
    fun inventoryEventPublisher(): InventoryEventPublisher =
        object : InventoryEventPublisher {
            override fun publishInventoryReserved(event: InventoryReservedEvent) {
                // Do nothing in tests
            }
        }
}