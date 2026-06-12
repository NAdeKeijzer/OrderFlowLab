package org.nikita.orderflowlab.payment.event

import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.nikita.orderflowlab.order.service.OrderService
import java.time.Instant
import java.util.*

class PaymentSucceededEventHandlerTest {

    @Test
    fun `confirms order when payment succeeded event is handled`() {
        val orderService = mock(OrderService::class.java)

        val handler = PaymentSucceededEventHandler(orderService)

        val event = PaymentSucceededEvent(
            orderId = UUID.randomUUID(),
            paidAt = Instant.now()
        )

        handler.handle(event)

        verify(orderService).confirm(event.orderId)
    }
}