package org.nikita.orderflowlab.outbox.service

import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxPublisherService(
    private val outboxEventRepository: OutboxEventRepository
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

            // Kafka publishing will be added in the next step.
            event.markPublished()
        }
    }
}