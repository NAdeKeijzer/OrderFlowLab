package org.nikita.orderflowlab.order.event

interface OrderEventPublisher {
    fun publishOrderCreated(event: OrderCreatedEvent)
}