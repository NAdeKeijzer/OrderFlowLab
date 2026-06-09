package org.nikita.orderflowlab.inventory.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.nikita.orderflowlab.order.service.OrderService
import java.time.Instant
import java.util.UUID

class InventoryReservationFailedEventHandlerTest {

    @Test
    fun `marks order as inventory failed when inventory reservation failed event is handled`() {
        val orderService = mock(OrderService::class.java)

        val handler = InventoryReservationFailedEventHandler(
            orderService = orderService
        )

        val event = InventoryReservationFailedEvent(
            orderId = UUID.randomUUID(),
            failedAt = Instant.parse("2026-05-13T12:42:29Z"),
            reason = "Inventory item not found"
        )

        handler.handle(event)

        verify(orderService).markInventoryFailed(event.orderId)
    }
}