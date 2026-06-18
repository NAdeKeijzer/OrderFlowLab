package org.nikita.orderflowlab.order.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.nikita.orderflowlab.inventory.service.InventoryReservationService
import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.order.exception.OrderNotFoundException
import org.nikita.orderflowlab.order.model.Order
import org.nikita.orderflowlab.order.model.OrderLineInput
import org.nikita.orderflowlab.order.repository.OrderRepository
import org.nikita.orderflowlab.outbox.service.OutboxEventService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderEventPublisher: OrderEventPublisher,
    private val inventoryReservationService: InventoryReservationService,
    private val outboxEventService: OutboxEventService,
    private val objectMapper: ObjectMapper
) {

    @Transactional
    fun createOrder(
        customerId: UUID,
        items: List<CreateOrderLineRequest>
    ): Order {
        val orderLineInputs = items.map {
            OrderLineInput(
                productId = it.productId,
                quantity = it.quantity,
                unitPrice = it.unitPrice
            )
        }

        val order = Order.create(
            customerId = customerId,
            items = orderLineInputs
        )

        val savedOrder = orderRepository.save(order)

        val orderCreatedEvent = OrderCreatedEvent(
            orderId = savedOrder.id,
            customerId = savedOrder.customerId,
            totalPrice = savedOrder.total(),
            createdAt = savedOrder.createdAt,
            lines = savedOrder.lines.map {
                OrderCreatedLineEvent(
                    productId = it.productId,
                    quantity = it.quantity
                )
            }
        )

        outboxEventService.save(
            eventType = "ORDER_CREATED",
            aggregateId = savedOrder.id,
            payload = objectMapper.writeValueAsString(orderCreatedEvent)
        )

        return savedOrder
    }

    @Transactional(readOnly = true)
    fun getOrder(id: UUID): Order =
        orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

    @Transactional(readOnly = true)
    fun getAll(): List<Order> =
        orderRepository.findAll()

    @Transactional
    fun markInventoryReserved(id: UUID): Order {
        val order = getOrder(id)

        order.markInventoryReserved()

        return orderRepository.save(order)
    }

    @Transactional
    fun markInventoryFailed(id: UUID): Order {
        val order = getOrder(id)

        order.markInventoryFailed()

        return orderRepository.save(order)
    }

    @Transactional
    fun confirm(id: UUID): Order {
        val order = getOrder(id)

        order.confirm()

        return orderRepository.save(order)
    }

    @Transactional
    fun pay(id: UUID): Order {
        val order = getOrder(id)

        order.markAsPaid()

        return orderRepository.save(order)
    }

    @Transactional
    fun cancel(id: UUID): Order {
        val order = getOrder(id)

        order.cancel()

        inventoryReservationService.releaseFor(order.id)

        return orderRepository.save(order)
    }

    @Transactional
    fun markPaymentFailed(id: UUID): Order {
        val order = getOrder(id)

        order.markPaymentFailed()

        return orderRepository.save(order)
    }
}