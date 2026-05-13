package org.nikita.orderflowlab.order.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.nikita.orderflowlab.inventory.InventoryReservationRepository
import org.nikita.orderflowlab.inventory.InventoryReservationService
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class OrderCreatedEventHandlerTest {

    @Test
    fun `handles order created event`() {
        val repository = mock(InventoryReservationRepository::class.java)

        val handler = OrderCreatedEventHandler(
            inventoryReservationService = InventoryReservationService(repository)
        )

        val event = OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            createdAt = Instant.parse("2026-05-13T12:42:29Z"),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = UUID.randomUUID(),
                    quantity = 2
                )
            )
        )

        handler.handle(event)
    }
}