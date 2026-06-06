package org.nikita.orderflowlab.inventory.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.nikita.orderflowlab.order.service.OrderService
import java.time.Instant
import java.util.*

class InventoryReservedEventHandlerTest {

    @Test
    fun `confirms order when inventory reserved event is handled`() {
        val orderService = mock(OrderService::class.java)

        val handler = InventoryReservedEventHandler(
            orderService = orderService
        )

        val event = InventoryReservedEvent(
            orderId = UUID.randomUUID(),
            reservedAt = Instant.parse("2026-05-13T12:42:29Z")
        )

        handler.handle(event)

        verify(orderService).confirm(event.orderId)
    }
}