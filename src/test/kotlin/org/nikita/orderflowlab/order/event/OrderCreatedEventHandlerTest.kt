package org.nikita.orderflowlab.order.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.service.OrderService
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

class OrderCreatedEventHandlerTest {

    @Test
    fun `marks order as inventory reserved and confirmed when reservation succeeds`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)

        val handler = OrderCreatedEventHandler(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService
        )

        val event = orderCreatedEvent()

        handler.handle(event)

        verify(inventoryReservationService).reserveFor(event)
        verify(orderService).markInventoryReserved(event.orderId)
        verify(orderService).confirm(event.orderId)
        verify(orderService, never()).markInventoryFailed(event.orderId)
    }

    @Test
    fun `marks order as inventory failed when reservation fails`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)

        val handler = OrderCreatedEventHandler(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService
        )

        val event = orderCreatedEvent()

        doThrow(
            InventoryItemNotFoundException(event.lines.first().productId)
        ).`when`(inventoryReservationService).reserveFor(event)

        handler.handle(event)

        verify(inventoryReservationService).reserveFor(event)
        verify(orderService).markInventoryFailed(event.orderId)
        verify(orderService, never()).markInventoryReserved(event.orderId)
        verify(orderService, never()).confirm(event.orderId)
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