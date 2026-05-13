package org.nikita.orderflowlab.order.event

import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class OrderCreatedEventHandlerTest {

    @Test
    fun `handles order created event`() {
        val handler = OrderCreatedEventHandler()

        val event = OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            createdAt = Instant.parse("2026-05-13T12:42:29Z")
        )

        handler.handle(event)
    }
}