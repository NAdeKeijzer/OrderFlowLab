package org.nikita.orderflowlab.inventory.event

interface InventoryEventPublisher {

    fun publishInventoryReserved(event: InventoryReservedEvent)

    fun publishInventoryReservationFailed(event: InventoryReservationFailedEvent)
}