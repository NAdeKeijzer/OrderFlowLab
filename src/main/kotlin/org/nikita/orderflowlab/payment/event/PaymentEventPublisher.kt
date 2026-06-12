package org.nikita.orderflowlab.payment.event

interface PaymentEventPublisher {

    fun publishPaymentRequested(event: PaymentRequestedEvent)

    fun publishPaymentSucceeded(event: PaymentSucceededEvent)
}