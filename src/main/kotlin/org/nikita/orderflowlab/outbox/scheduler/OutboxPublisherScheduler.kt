package org.nikita.orderflowlab.outbox.scheduler

import org.nikita.orderflowlab.outbox.service.OutboxPublisherService
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
@Profile("postgres")
class OutboxPublisherScheduler(
    private val outboxPublisherService: OutboxPublisherService
) {

    @Scheduled(fixedDelayString = "\${outbox.publisher.fixed-delay-ms:5000}")
    fun publishPendingEvents() {
        outboxPublisherService.publishPendingEvents()
    }
}