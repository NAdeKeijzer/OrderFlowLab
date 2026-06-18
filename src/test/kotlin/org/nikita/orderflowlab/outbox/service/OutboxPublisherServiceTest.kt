package org.nikita.orderflowlab.outbox.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.outbox.model.OutboxEvent
import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@DataJpaTest
class OutboxPublisherServiceTest @Autowired constructor(
    private val outboxEventRepository: OutboxEventRepository
) {

    private val publishedOrderCreatedEvents = mutableListOf<OrderCreatedEvent>()

    private val orderEventPublisher = object : OrderEventPublisher {
        override fun publishOrderCreated(event: OrderCreatedEvent) {
            publishedOrderCreatedEvents += event
        }
    }

    private val objectMapper = ObjectMapper().findAndRegisterModules()

    @Test
    fun `publishes pending order created events and marks them as published`() {
        val orderCreatedEvent = orderCreatedEvent()

        val event = outboxEventRepository.save(
            OutboxEvent(
                eventType = "ORDER_CREATED",
                aggregateId = orderCreatedEvent.orderId,
                payload = objectMapper.writeValueAsString(orderCreatedEvent)
            )
        )

        val service = OutboxPublisherService(
            outboxEventRepository = outboxEventRepository,
            orderEventPublisher = orderEventPublisher,
            objectMapper = objectMapper
        )

        service.publishPendingEvents()

        val updatedEvent = outboxEventRepository.findById(event.id).orElseThrow()

        assertThat(publishedOrderCreatedEvents).hasSize(1)
        assertThat(publishedOrderCreatedEvents.first().orderId).isEqualTo(orderCreatedEvent.orderId)
        assertThat(updatedEvent.publishedAt).isNotNull()
    }

    private fun orderCreatedEvent(): OrderCreatedEvent =
        OrderCreatedEvent(
            orderId = UUID.randomUUID(),
            customerId = UUID.randomUUID(),
            totalPrice = BigDecimal("19.98"),
            createdAt = Instant.now(),
            lines = listOf(
                OrderCreatedLineEvent(
                    productId = UUID.randomUUID(),
                    quantity = 2
                )
            )
        )
}