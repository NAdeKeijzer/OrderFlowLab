package org.nikita.orderflowlab.outbox.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.outbox.model.OutboxEvent
import org.nikita.orderflowlab.outbox.repository.OutboxEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.util.*

@DataJpaTest
class OutboxPublisherServiceTest @Autowired constructor(
    private val outboxEventRepository: OutboxEventRepository
) {

    @Test
    fun `marks pending outbox events as published`() {
        val event = outboxEventRepository.save(
            OutboxEvent(
                eventType = "ORDER_CREATED",
                aggregateId = UUID.randomUUID(),
                payload = """{"type":"ORDER_CREATED"}"""
            )
        )

        val service = OutboxPublisherService(
            outboxEventRepository = outboxEventRepository
        )

        service.publishPendingEvents()

        val updatedEvent = outboxEventRepository.findById(event.id).orElseThrow()

        assertThat(updatedEvent.publishedAt).isNotNull()
    }
}