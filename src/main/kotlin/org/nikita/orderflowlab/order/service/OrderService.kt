package org.nikita.orderflowlab.order.service

import org.nikita.orderflowlab.order.dto.CreateOrderLineRequest
import org.nikita.orderflowlab.order.event.OrderCreatedEvent
import org.nikita.orderflowlab.order.event.OrderCreatedLineEvent
import org.nikita.orderflowlab.order.event.OrderEventPublisher
import org.nikita.orderflowlab.order.exception.OrderNotFoundException
import org.nikita.orderflowlab.order.model.Order
import org.nikita.orderflowlab.order.model.OrderLineInput
import org.nikita.orderflowlab.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val orderEventPublisher: OrderEventPublisher
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

        orderEventPublisher.publishOrderCreated(
            OrderCreatedEvent(
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
        )
        return savedOrder
    }

    @Transactional(readOnly = true)
    fun getOrder(id: UUID): Order {
        return orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }
    }

    @Transactional(readOnly = true)
    fun getAll(): List<Order> {
        return orderRepository.findAll()
    }
    @Transactional
    fun pay(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

        order.markAsPaid()

        return orderRepository.save(order)
    }

    @Transactional
    fun markInventoryFailed(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

        order.markInventoryFailed()

        return orderRepository.save(order)
    }

    @Transactional
    fun cancel(id: UUID): Order {
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException(id) }

        order.cancel()

        return orderRepository.save(order)
    }

}