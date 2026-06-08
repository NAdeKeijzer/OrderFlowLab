package org.nikita.orderflowlab.order.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.nikita.orderflowlab.inventory.event.InventoryEventPublisher
import org.nikita.orderflowlab.inventory.event.InventoryReservationFailedEvent
import org.nikita.orderflowlab.inventory.event.InventoryReservedEvent
import org.nikita.orderflowlab.inventory.exception.InventoryItemNotFoundException
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class OrderWorkflowServiceTest {

    @Test
    fun `marks order as inventory reserved when reservation succeeds`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)
        val inventoryEventPublisher = FakeInventoryEventPublisher()

        val workflowService = OrderWorkflowService(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService,
            inventoryEventPublisher = inventoryEventPublisher
        )

        val event = orderCreatedEvent()

        workflowService.handleOrderCreated(event)

        verify(inventoryReservationService).reserveFor(event)
        verify(orderService).markInventoryReserved(event.orderId)
        verify(orderService, never()).confirm(event.orderId)
        verify(orderService, never()).markInventoryFailed(event.orderId)

        assertThat(inventoryEventPublisher.failedEvents).isEmpty()
    }

    @Test
    fun `publishes inventory reservation failed event when reservation fails`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)
        val inventoryEventPublisher = FakeInventoryEventPublisher()

        val workflowService = OrderWorkflowService(
            inventoryReservationService = inventoryReservationService,
            orderService = orderService,
            inventoryEventPublisher = inventoryEventPublisher
        )

        val event = orderCreatedEvent()

        doThrow(
            InventoryItemNotFoundException(event.lines.first().productId)
        ).`when`(inventoryReservationService).reserveFor(event)

        workflowService.handleOrderCreated(event)

        verify(inventoryReservationService).reserveFor(event)
        verify(orderService, never()).markInventoryFailed(event.orderId)
        verify(orderService, never()).markInventoryReserved(event.orderId)
        verify(orderService, never()).confirm(event.orderId)

        assertThat(inventoryEventPublisher.failedEvents).hasSize(1)
        assertThat(inventoryEventPublisher.failedEvents.first().orderId)
            .isEqualTo(event.orderId)
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

    private class FakeInventoryEventPublisher : InventoryEventPublisher {
        val reservedEvents = mutableListOf<InventoryReservedEvent>()
        val failedEvents = mutableListOf<InventoryReservationFailedEvent>()

        override fun publishInventoryReserved(event: InventoryReservedEvent) {
            reservedEvents += event
        }

        override fun publishInventoryReservationFailed(event: InventoryReservationFailedEvent) {
            failedEvents += event
        }
    }
}