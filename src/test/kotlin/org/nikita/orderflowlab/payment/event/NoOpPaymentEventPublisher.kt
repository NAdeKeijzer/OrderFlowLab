package org.nikita.orderflowlab.payment.event

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class NoOpPaymentEventPublisher {

    @Bean
    fun paymentEventPublisher(): PaymentEventPublisher =
        object : PaymentEventPublisher {
            override fun publishPaymentRequested(event: PaymentRequestedEvent) {
                // Do nothing in tests
            }

            override fun publishPaymentSucceeded(event: PaymentSucceededEvent) {
                // Do nothing in tests
            }
        }
}