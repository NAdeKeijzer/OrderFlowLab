package org.nikita.orderflowlab.outbox.service

import org.nikita.orderflowlab.outbox.model.OutboxEvent
import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class OutboxEventService(
    private val outboxEventRepository: OutboxEventRepository
) {

    fun save(
        eventType: String,
        aggregateId: UUID,
        payload: String
    ): OutboxEvent =
        outboxEventRepository.save(
            OutboxEvent(
                eventType = eventType,
                aggregateId = aggregateId,
                payload = payload
            )
        )
}