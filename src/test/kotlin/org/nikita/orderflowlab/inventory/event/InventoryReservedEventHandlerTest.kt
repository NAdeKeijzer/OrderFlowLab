package org.nikita.orderflowlab.inventory.event

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.payment.event.PaymentEventPublisher
import org.nikita.orderflowlab.payment.event.PaymentRequestedEvent
import org.nikita.orderflowlab.payment.event.PaymentSucceededEvent
import java.math.BigDecimal
import java.time.Instant
import java.util.*

class InventoryReservedEventHandlerTest {

    @Test
    fun `publishes payment requested event when inventory reserved event is handled`() {
        val publisher = FakePaymentEventPublisher()

        val handler = InventoryReservedEventHandler(
            paymentEventPublisher = publisher
        )

        val event = InventoryReservedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("25.48"),
            reservedAt = Instant.parse("2026-05-13T12:42:29Z")
        )

        handler.handle(event)

        assertThat(publisher.publishedEvents).hasSize(1)

        val published = publisher.publishedEvents.first()

        assertThat(published.orderId).isEqualTo(event.orderId)
        assertThat(published.customerId).isEqualTo(event.customerId)
        assertThat(published.amount).isEqualTo(event.totalPrice)
    }

    private class FakePaymentEventPublisher : PaymentEventPublisher {

        val publishedEvents = mutableListOf<PaymentRequestedEvent>()

        override fun publishPaymentRequested(event: PaymentRequestedEvent) {
            publishedEvents += event
        }

        override fun publishPaymentSucceeded(event: PaymentSucceededEvent) {
            // not used in this test
        }
    }
}