package org.nikita.orderflowlab.order.event

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class NoOpOrderEventPublisher {

    @Bean
    fun orderEventPublisher(): OrderEventPublisher =
        object : OrderEventPublisher {
            override fun publishOrderCreated(event: OrderCreatedEvent) {
                // Do nothing in tests
            }
        }
}