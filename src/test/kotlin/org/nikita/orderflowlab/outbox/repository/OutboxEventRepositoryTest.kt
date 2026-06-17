package org.nikita.orderflowlab.outbox.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.nikita.orderflowlab.outbox.model.OutboxEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import java.util.*

@DataJpaTest
class OutboxEventRepositoryTest @Autowired constructor(
    private val outboxEventRepository: OutboxEventRepository
) {

    @Test
    fun `saves outbox event`() {
        val event = OutboxEvent(
            eventType = "ORDER_CREATED",
            aggregateId = UUID.randomUUID(),
            payload = """{"type":"ORDER_CREATED"}"""
        )

        val savedEvent = outboxEventRepository.save(event)

        assertThat(savedEvent.id).isNotNull()
        assertThat(savedEvent.eventType).isEqualTo("ORDER_CREATED")
        assertThat(savedEvent.publishedAt).isNull()
    }
}