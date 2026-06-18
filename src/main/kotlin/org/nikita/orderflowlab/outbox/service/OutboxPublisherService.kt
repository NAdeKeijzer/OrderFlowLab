package org.nikita.orderflowlab.outbox.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxPublisherService(
    private val outboxEventRepository: OutboxEventRepository,
    private val orderEventPublisher: OrderEventPublisher,
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(OutboxPublisherService::class.java)

    @Transactional
    fun publishPendingEvents() {
        val pendingEvents = outboxEventRepository.findAllByPublishedAtIsNull()

        pendingEvents.forEach { event ->
            logger.info(
                "Publishing outbox event id={}, type={}, aggregateId={}",
                event.id,
                event.eventType,
                event.aggregateId
            )

            when (event.eventType) {
                "ORDER_CREATED" -> {
                    val orderCreatedEvent = objectMapper.readValue(
                        event.payload,
                        OrderCreatedEvent::class.java
                    )

                    orderEventPublisher.publishOrderCreated(orderCreatedEvent)
                    event.markPublished()
                }

                else -> logger.warn(
                    "Unsupported outbox event type: {}",
                    event.eventType
                )
            }
        }
    }
}