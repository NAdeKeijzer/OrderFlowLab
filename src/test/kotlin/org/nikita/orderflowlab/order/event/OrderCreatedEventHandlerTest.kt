package org.nikita.orderflowlab.order.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.service.OrderService
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class OrderCreatedEventHandlerTest {

    @Test
    fun `handles order created event`() {

        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)

        val handler = OrderCreatedEventHandler(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService
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

        verify(inventoryReservationService).reserveFor(event)
    }

    @Test
    fun `marks order as inventory failed when reservation fails`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)

        val handler = OrderCreatedEventHandler(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService
        )

        val event = OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            createdAt = Instant.parse("2026-05-13T12:42:29Z"),
            lines = emptyList()
        )

        doThrow(
            InventoryItemNotFoundException(UUID.randomUUID())
        ).`when`(inventoryReservationService).reserveFor(event)

        handler.handle(event)

        verify(orderService).markInventoryFailed(event.orderId)
    }
}