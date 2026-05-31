package org.nikita.orderflowlab.order.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.nikita.orderflowlab.order.service.OrderWorkflowService
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class OrderCreatedEventHandlerTest {

    @Test
    fun `delegates order created event to workflow service`() {
        val orderWorkflowService = mock(OrderWorkflowService::class.java)

        val handler = OrderCreatedEventHandler(
            orderWorkflowService = orderWorkflowService
        )

        val event = orderCreatedEvent()

        handler.handle(event)

        verify(orderWorkflowService).handleOrderCreated(event)
    }

    private fun orderCreatedEvent(): OrderCreatedEvent =
        OrderCreatedEvent(
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
}