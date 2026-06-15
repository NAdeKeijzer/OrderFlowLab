package org.nikita.orderflowlab.payment.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.service.OrderService
import java.time.Instant
import java.util.UUID

class PaymentFailedEventHandlerTest {

    @Test
    fun `releases inventory and marks order as payment failed`() {
        val inventoryReservationService = mock(InventoryReservationService::class.java)
        val orderService = mock(OrderService::class.java)

        val handler = PaymentFailedEventHandler(
            inventoryReservationService,
            orderService
        )

        val event = PaymentFailedEvent(
            orderId = UUID.randomUUID(),
            failedAt = Instant.now(),
            reason = "Card declined"
        )

        handler.handle(event)

        verify(inventoryReservationService).releaseFor(event.orderId)
        verify(orderService).markPaymentFailed(event.orderId)
    }
}